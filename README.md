# Desafio Pismo

API desenvolvida em Groovy utilizando o framework Grails para simular transações em uma conta digital.

+ Disponível em: [link]

## Instruções para execução local

A aplicação já se encontra disponível para testes no link acima. Caso vocẽ tenha o desejo de executar a aplicação localmente, uma vez clonado, os passos são:

Instalar <a href="https://docs.grails.org/latest/guide/gettingStarted.html">Grails Framework Versão 2.1.0</a>

Instalar <a href="https://www.postgresql.org/download/">PostgreSQL</a>. Banco padrão: "pismo_development"

Git clone do projeto: https://github.com/derickrosa/Teste-Pismo.git

Vá até o diretório da aplicação: ```cd ~/desafioPismo.```

Executar o comando ```grails run-app```.

Não há necessidade de criar um arquivo WAR ou executar o deploy da aplicação.

A aplicação deverá estar rodando em http://localhost:8080/desafioPismo.

## Decisões de projeto

+ #### Diagrama UML

<p align="center">
  <img src="https://github.com/derickrosa/Teste-Pismo/blob/master/diagrama_classes_account.png">
</p>

## Possíveis Melhorias

+ #### Testes

Por conta do pouco tempo de desenvolvimento disponível, a aplicação não possui testes automatizados. Obviamente, em uma aplicação que realmente fosse para produção, a presença de testes seria indispensável.

+ #### Segurança

O mesmo é valído para a questão da segurança, devido ao tempo e para facilitar o acesso não houve implementação nesse sentido. APIs desprotegidas ou hackeadas são a causa das principais violações de dados, e de suma importância quando se trata de sistemas financeiros.
