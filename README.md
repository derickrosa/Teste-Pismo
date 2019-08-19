# Desafio Pismo

API desenvolvida em Groovy utilizando o framework Grails para simular transações em uma conta digital.

+ Disponível em: [link]

## Instruções para execução local

A aplicação já se encontra disponível para testes no link acima. Caso vocẽ tenha o desejo de executar a aplicação localmente, uma vez clonado, os passos são:

Instalar <a href="https://docs.grails.org/latest/guide/gettingStarted.html">Grails Framework Versão 3.3.0 /Groovy 2.4.11</a>

Instalar <a href="https://www.postgresql.org/download/">PostgreSQL</a>. Banco padrão: "pismo_development"

Git clone do projeto: https://github.com/derickrosa/Teste-Pismo.git

Vá até o diretório da aplicação: ```cd ~/desafioPismo.```

Executar o comando ```grails run-app```.

Não há necessidade de criar um arquivo WAR ou executar o deploy da aplicação.

A aplicação deverá estar rodando em http://localhost:8080/testePismo.

## Decisões de projeto

+ #### FrameWork Grails
Por já estar habituado com a tecnologia e com o intuito de agilizar o desenvolvimento , optei por desenvolver o projeto utilizando o framework Grails versão 3.3.0 com Groovy 2.4.11.

+ #### Estrutura do projeto
Este projeto utiliza controllers para processar e validar as requisições, services para processar os dados e classes para representar os modelos de dados. 

## Decisões de Negócio

+ #### PATCH => ~/testePismo/account/update/<id>
  
Este endpoint receberá valores de limite para available_credit_limit e available_withdrawal_limit e abate os respectivos amount dos saldos da conta <id>.
  
+ #### GET   => ~/testePismo/account/limits

Este endpoint retorna uma lista de contas "accounts" com as respectivas informações de limite.
  
+ #### POST  => ~/testePismo/transaction

Este endpoint recebe as informações de uma transação em um conta e cria uma transaction, o valor da transação é automaticamente abatido de ambos os limites da conta, se a conta com o id informado não existir, uma nova conta é criada com saldo negativo equivalente ao valor da transação.

Foi criado um setup no projeto para pré cadastrar os tipos de operação suportados, que foram retirados do comando do desafio.
<p align="center">
  <img src="https://github.com/derickrosa/Teste-Pismo/blob/master/tabela_operation_type.png">
</p>
  
+ #### POST  => ~/testePismo/payment

Este endpoint recebe uma lista de pagamentos a serem abatidos das transações da conta informada em cada pagamento. É criado uma transação para cada pagamento "TransactionPayment". É realizado uma busca pelas transações válidas para serem abatidas de acordo com a conta, o status não pago (foi adotado a nomenclatura "PURCHASED"). Esta transações são ordenadas de acordo com a prioridade e então tem o saldo abatido de acordo com o valor disponível no pagamento e se quitadas tem o status alterado para paga. Ao final, se ainda tiver saldo este saldo é acrescentado a ambos limites da conta e a transação de pagamento é alterada também paga "PAID". 

Criei uma classe extrata "PaymentTransaction" para armazenar as informações de pagamento da transações de maneira que fosse possível mapear quais transações foram abatidas por um determinado pagamento e por quais pagamentos uma transação foi quitada.

<p align="center">
  <img src="https://github.com/derickrosa/Teste-Pismo/blob/master/diagrama_classes_account.png">
</p>

## Possíveis Melhorias

+ #### Testes

Por conta do pouco tempo de desenvolvimento disponível, a aplicação não possui testes automatizados. Obviamente, em uma aplicação que realmente fosse para produção, a presença de testes seria indispensável.

+ #### Segurança

O mesmo é valído para a questão da segurança, devido ao tempo não houve implementação nesse sentido. APIs desprotegidas ou hackeadas são a causa das principais violações de dados, e de suma importância quando se trata de sistemas financeiros.
