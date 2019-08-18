package com.pismo.cadastro

import com.pismo.cadastro.Account
import com.pismo.cadastro.OperationType
import com.pismo.cadastro.PaymentTransaction
import com.pismo.cadastro.Transaction
import com.pismo.cadastro.TransactionCommand
import com.pismo.cadastro.TransactionPayment
import enums.StatusTransaction

class PaymentService {
    def parametrosCorte = [:]
    def sessionFactory

    def clearParametros() {
        parametrosCorte = [:]
    }
    def addParametro(key, value) {
        parametrosCorte[key] = value
    }

    def save(List<TransactionCommand> paymentCommandList) {
        List<PaymentTransaction> paymentList = []
        paymentCommandList.each{paymentCommand->
            paymentList << save(paymentCommand)
        }
        paymentList
    }
    def save(TransactionCommand transactionCommand){
        PaymentTransaction paymentTransactionInstance = new PaymentTransaction()
        Account accountInstance = Account.findById(transactionCommand.account_id)
        if(!accountInstance){
            accountInstance = new Account(transactionCommand.amount)
        }
        paymentTransactionInstance.account = accountInstance
        OperationType operationTypeInstance = OperationType.get(transactionCommand.operation_type_id)
        paymentTransactionInstance.operationType = operationTypeInstance
        paymentTransactionInstance.eventdate = new Date()
        paymentTransactionInstance.paidDate = transactionCommand.paid_date
        paymentTransactionInstance.amount = transactionCommand.amount
        paymentTransactionInstance.balance = transactionCommand.amount
        accountInstance.addToTransactions(paymentTransactionInstance)
        accountInstance.save(flush: true, failOnError: true)
        billing(paymentTransactionInstance)
        paymentTransactionInstance
    }

    def billing(PaymentTransaction paymentTransaction){
        //Suporta pagamento customizado de transações específicas por eventDate, dueDate e OperationType
        def hql = '''select t
                from com.pismo.cadastro.Transaction t
                where t.operationType.id<>4
                and t.statusTransaction ='PURCHASED'
                and t.account= :account '''
        def pars = [:]
        if(parametrosCorte['account_id'])
            pars['account'] = parametrosCorte['account_id']
        else
            pars['account'] = paymentTransaction.account
        if (parametrosCorte['max'])
            pars['max']=parametrosCorte['max']
        if (parametrosCorte['eventDate']) {
            hql += ' and t.eventDate=:eventDate '
            pars['eventDate'] = parametrosCorte['eventDate']
        }
        if (parametrosCorte['dueDate']) {
            hql += ' and t.dueDate=:dueDate '
            pars['dueDate'] = parametrosCorte['dueDate']
        }
        if (parametrosCorte['operation_type_id']) {
            hql += ' and t.operation_type_id=:operation_type '
            pars['operation_type_id'] = parametrosCorte['operation_type_id']
        }

        log.debug "Obtendo transactions..."
        log.debug "HQL: $hql"
        log.debug "PAR: $pars"
        def transactionList = Transaction.executeQuery(hql, pars).sort{ x, y->
            if(x.operationType.chargeOrder == y.operationType.chargeOrder){
                x.eventdate <=> y.eventdate
            }else{
                x.operationType.chargeOrder <=> y.operationType.chargeOrder
            }
        }.reverse()

        transactionList = transactionList.collect{it.id}
        println "Lista de transacoes: $transactionList"
        charge(transactionList, paymentTransaction)
        clearParametros()
    }

    def charge(List<Long> transactionList, PaymentTransaction paymentTransaction){
        println "Chegou no charge: $transactionList - pay:$paymentTransaction.amount"
        int idx = 0
        for(Long id in transactionList){
            Transaction transaction = Transaction.get(id)
            def balance = transaction.balance + paymentTransaction.balance
            println "Chegou no loop inicial: bal: $balance - trns: $transaction.balance  -  pay:$paymentTransaction.balance"
            if(balance > 0){
                println("Balance maior que zero")
                TransactionPayment transactionPayment = new TransactionPayment()
                transactionPayment.paymentAmount = transaction.balance
                transaction.addToBillings(transactionPayment)
                paymentTransaction.addToBillings(transactionPayment)
                transaction.balance = 0
                transaction.statusTransaction = StatusTransaction.PAID
                paymentTransaction.balance = balance
                transaction.save(failOnError:true)
                paymentTransaction.save(failOnError:true)
                println "Chegou no final int: bal: $balance - trns: $transaction.balance  -  pay:$paymentTransaction.balance"
            } else if(balance < 0){
                println("Balance menor que zero")
                TransactionPayment transactionPayment = new TransactionPayment()
                transactionPayment.paymentAmount = paymentTransaction.balance
                transaction.addToBillings(transactionPayment)
                paymentTransaction.addToBillings(transactionPayment)
                transaction.balance = balance
                paymentTransaction.balance = 0
                transaction.statusTransaction = StatusTransaction.PAID
                transaction.save(flush: true, failOnError:true)
                paymentTransaction.save(failOnError:true)
                println "Cabou pagamentos: bal: $balance - trns: $transaction.balance  -  pay:$paymentTransaction.balance"
                break
            } else {
                println("Igual a zero")
                TransactionPayment transactionPayment = new TransactionPayment()
                transactionPayment.paymentAmount = paymentTransaction.balance
                transaction.addToBillings(transactionPayment)
                transaction.balance = 0
                paymentTransaction.balance = 0
                transaction.save(flush: true, failOnError:true)
                paymentTransaction.addToBillings(transactionPayment)
                paymentTransaction.save(flush: true, failOnError:true)
                println "Cabou pagamentos: bal: $balance - trns: $transaction.balance  -  pay:$paymentTransaction.balance"
                break
            }
            transaction.save(flush: true, failOnError:true)
            paymentTransaction.save(flush: true, failOnError:true)
            println "----"
            idx++
            if(idx % 100 == 0) gormClean()
        }
        paymentTransaction.statusTransaction = StatusTransaction.PAID
        paymentTransaction.save(flush: true, failOnError:true)

        def account = paymentTransaction.account
        account.availableCreditLimit += paymentTransaction.amount
        account.availableWithdrawalLimit += paymentTransaction.amount
        account.save(flush: true, failOnError: true)

    }

    def gormClean(Boolean flush = true) {
        log.trace "Clearing..."
        if (flush) {
            sessionFactory.getCurrentSession().flush()
        }
        sessionFactory.getCurrentSession().clear()

    }
}
