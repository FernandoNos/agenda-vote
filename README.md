# Votação em Pautas

O objetivo deste projeto era a criação de interfaces REST que possibilitassem as seguintes funções:
 1. Criação de associados;
 2. Criação de pautas;
 3. Inicialização de pautas (definição de uma duração);
 4. Busca de resultado de uma determinada pauta.

## Detalhes

[Manjaro OS](https://manjaro.org/) - Sistema de Operação utilizado durante o desenvolvimento

[Intellij](https://www.jetbrains.com/idea/) - IDE utilizada para desenvolvimento: Selecionada pela facilidade de utilização, somada à fácil integração com aplicações Spring.

[Gradle](https://gradle.org/) - Ferramenta de automação de builds: Selecionado pela fácil manutenção e inclusão de bibliotecas, assim como definição de diferentes escopos de execução.

[Java](https://www.java.com/en/download/) - Linguagem de Programação

[JUnit](https://junit.org/junit5/) - Framework de Teste: Selecionado para desenvolvimento de cenários de teste, de forma a assegurar o resultado desejado.

[Jacoco](https://www.eclemma.org/jacoco/) - Biblioteca de cobertura de código: Selecionado para geração de relatório de cobertura, de forma a assegurar que ramos do código não fossem ignorados pelos cenários de teste.

[SpringBoot](https://spring.io/projects/spring-boot) - Framework para desenvolvimento de aplicações Spring: Selecionado pela fácil geração de novas aplicações.

[MongoDB](https://www.mongodb.com/) - Sistema operacional utilizado para desenvolvimento: Visto que a aplicação se tratava de uma aplicação Rest, MongoDB se torna uma escolha fácil, dada a sua capacidade de utilizar documentos para armazenamento de informações, não sendo necessário que a camada do banco de dados tenha uma representação, do ponto de vista de manipulação dos dados, diferente da camada de negócio, e view. Durante o desenvolvimento, foi utilizado um banco em memória, que era criado juntamente com a aplicação, durante o startup.

[Docker](https://www.docker.com/) -  Selecionado para a fácil disponibilização de um ambiente estável para a execução da aplicação, possibilitando a abstração dos passos de configuração do ambiente local.

[Docker-Compose](https://docs.docker.com/compose/) - Selecionado para fazer a conexão entre a aplicação e o banco de dados MongoDB, assim como manter os dados salvos em banco de dados entre restarts da aplicação. Também possíbilita o controle de dependência entre aplicações, nesse caso, a existência da dependência entre a aplicação spring-boot e o banco MongoDB (visto que este deveria ser inicializado primeiramente).

[SLF4J](https://www.slf4j.org/) - Biblioteca para geração de logs

## Estratégia para desenvolvimento

Abaixo encontra-se o passo-a-passo utilizado durante o desenvolvimento:

1. Definição do ambiente que seria usado (springBoot, Docker, MongoDB);
2. Pesquisa e pequenas provas de conceito para validação do plano;
3. Criação do repositório Git;
4. Início do desenvolvimento:
     - Criação de associados
       - Dado que cada voto somente pode ocorrer dada a presença de um associado, uma interface para a criação do mesmo se fazia necessária. Visto que somente o cadastro do mesmo era necessário, inclui-se um nome e um identificador ao objeto, o qual passou a configurar o associado. 
     - Criação de pautas:
       - Para a criação de pautas, era necessária a definição de um título, e uma descrição, sendo estes os valores necessários para a solicitação de criação da mesma.
     - Definição de duração de uma pauta:
       - Cada pauta somente poderia receber votos durante uma janela de tempo, a qual é definida pelo valor de sua duração (definida em minutos, sendo o padrão 1 minuto).
     - Recebimento de votos:
       - Para a definição de um voto, é necessário que sejam informados o identificador do associado, o identificador da pauta, e o valor do voto (YES, or NO).
5. Revisão dos cenários e desenvolvimento de testes:

No início do desenvolvimento, algumas dúvidas sugiram quanto ao modo como determinados detalhes das funcionalidades deveriam funcionar (as quais foram sanadas ao conversar com a equipe de selação). 

  Tendo sido feito o desenvolvimento, busquei identificar os pontos cruciais de execução e tê-los mapeados em testes (refinando possíveis falhas encontradas em testes previamente desenvolvidos). Achei interessante também incluir testes das APIs Rest, que passam pelas mesmas classes testadas na bateria de objetos de negócio, porém agora da perspectiva de aplicações clientes e usuários.

## Estrutura das interfaces

Dados os cenários previamente listados, as seguintes interfaces foram desenvolvidas:

1.**/associate:**
     - POST: para criação`{"name":"Associate Name"}`
       - Response: 
          ```
    {
    "code": 100,
    "message": "SUCCESS",
      "transaction_details": {
        "id": "<ID do associado criado no banco>",
        "name": "<Nome enviado no request>"
      }
    }
          ```
     
     

1.**/agenda/:**
     - POST : para criação `{"agenda_title":"Agenda Title", "agenda_description":"Agenda Description"}`
            - Response: 
          ```
         {
          "code": 100,
          "message": "SUCCESS",
          "transaction_details": {
            "id": "<ID da Agenda criada no banco>",
            "votes": {},
            "creationDate": "<Data de criação, baseado no momento da requisição>",
            "agenda_description": "<Descrição da agenda enviada na requisição>",
            "agenda_title": "<Título da agenda enviada na requisição>",
            "agenda_duration": <Duração da agenda, que sempre será 0 neste momento>
          }
        }
          ```
    
2.**/agenda/<agenda ID>?duration=<duração deseja em minutos>:**
     - PUT : para alteração do valor da pauta, indicando que esta está aberta à votação. Caso uma duração não seja informada, 1 minuto será automaticamente definido como duração.
            - Response: 
          ```json
         {
          "code": 100,
          "message": "SUCCESS",
          "transaction_details": {
            "id": "<ID da Agenda criada no banco>",
            "votes": {},
            "creationDate": "<Data de criação, baseado no momento da requisição>",
            "agenda_description": "<Descrição da agenda enviada na requisição>",
            "agenda_title": "<Título da agenda enviada na requisição>",
            "agenda_duration": <Duração da agenda, que sempre deverá ser o mesmo valor fornecido>
          }
        }
          ```

3.**/agenda<agenda ID>/vote?associate_id=<associate Id>&vote=<vote>:**
     - PUT: para alteração da pauta, incluindo um novo possível voto (dado que esta ainda esteja aberta, e que o associado não tenha efetuado nenhum voto). 
            - Response: 
          ```
    {
      "code": 100,
      "message": "SUCCESS",
      "transaction_details": null
    }
          ```
  
4.**/agenda/<agenda ID>/result:**
     - GET: para recuperação do resultado da votação (somente retornará o resultado caso a votação já tenha sido finalizada).
            - Response: 
          ```
{
    "code": 100,
    "message": "SUCCESS",
    "transaction_details": {
        "id": "<ID da agenda solicitada>",
        "norm_title": "<Título da agenda solicitada>",
        "votes": {
            "YES": <Número de votos YES recebidos>,
            "NO": <Número de votos NO recebiso>
        },
        "creation_date": <Horário de criação da Agenda>,
        "closing_date": <Horário de término da Agenda>,
        "norm_description": <Descrição da Agenda>
    }
}
          ```

Os possíveis cenários de erro foram mapeados da forma abaixo. Estes valores serão refletidos nos campos "code" e "message" na resposta de cada interface, sendo aplicados de acordo com o cenário.

SUCCESS Código: 100 - Descrição: SUCCESS

NEGATIVE_DURATION Código: -1 - Descrição: The duration must be greater than 0!

VOTING_COMPLETED Código: 5 - Descrição: The voting has been completed for Agenda %s - %s! Closed at %s

AGENDA_NOT_FOUND Código: -10 - Descrição: The Agenda was not found!

ASSOCIATE_NOT_FOUND Código: -10 - Descrição: The associate id %s was not found!

ALREADY_VOTED Código: 7 - Descrição: You may only vote once Associate %s!

ERROR  Código: -100 - Descrição: An Error happened! Please reach out to your System Administrator!

DURATION_ALREADY_SET Código: 3 - Descrição: You MAY NOT reset the duration of an Agenda (%s - %s)!

VOTING_NOT_COMPLETED Código: 4 - Descrição: The Agenda %s - %s is still open for voting!

VOTING_NOT_STARTED Código: 5 - Descrição: Voting hasn't started for Agenda id %s - %s

INVALID_AGENDA Código: 12 - Descrição: Agenda MUST have a Title and a Description!

INVALID_ASSOCIATE Código: 13 - Descrição: Associate MUST have a Name

## Execução

### Linux
1. Instale Docker (versão utilizada 18.06.0-ce)
2. Instale Docker-Compose (versão utilizada 1.22.0)
3. Descompacte o zip fornecido (agenda-vote-prod-docker.zip)
4. Execute `sudo systemctl start docker`
5. Execute `sudo docker-compose build`
6. Execute `sudo docker-compose up`

7. Tendo sido feita a inicialização com sucesso, as interfaces estarão disponíveis nas URLs abaixo:
-POST: http://localhost:8183/agenda/
-PUT: http://localhost:8183/agenda/<Id da Agenda>?duration=5
-PUT: http://localhost:8183/agenda/<Id da Agenda >?duration=5
-PUT: http://localhost:8182/agenda/<Id da Agenda>/vote?associate_id=<Id do associate>&vote=YES
-GET: http://localhost:8183/agenda/<Id da Agenda>/result
  
## Relatório de testes
Como dito anteriormente, para os testes foram utilizados JUnit e Jacoco, para implementação de testes e geração de relatório de cobertura respectivamente. JUnit foi utilizado para o mapeamento de cenários de utilização em testes automatizados, sendo estes parte da execução do processo de build, de forma que possíveis defeitos, anteriormente não existentes, seriam identificados. Jacoco foi utilizado para a identificação de cobertura de código, de forma que não somente os cenários mapeados estavam cobertos, mas também como todos os possíveis ramos do código. 

** Não foi tido como objetivo do exercício o mapeamento de todos os possíveis cenários, tendo sido somente cobertos os identificados como mais críticos

## Pendente
* Revisão dos logs e possível melhoria de mapeamento de cenários em logs.
