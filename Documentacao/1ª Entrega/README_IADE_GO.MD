# IADE GO - [IADE](https://www.iade.europeia.pt/) - [UE](https://www.europeia.pt/)


**Universidade:** [Universidade Europeia](https://www.europeia.pt/)
**Faculdade:** [IADE - Faculdade de Design, Tecnologia e Comunicação](https://www.iade.europeia.pt/)
**Repositório:** [IADE GO](https://github.com/mimofixe/Projeto_3o_Semestre)
**Curso:** Engenharia Informática

## Índice

* [Elementos do Grupo](#elementos-do-grupo)
* [Poster](#poster)
* [Palavras-Chave](#palavras-chave)
* [Descrição](#descrição)

  * [Motivação](#motivação)
  * [Objetivos](#objetivos)
* [Público-Alvo](#público-alvo)
* [Pesquisa de Mercado](#pesquisa-de-mercado)
* [Guiões de Teste](#guiões-de-teste)
* [Plano de Trabalho](#plano-de-trabalho)
* [Descrição da Solução e Enquadramento das Unidades Curriculares](#descrição-da-solução-e-enquadramento-das-unidades-curriculares)
* [Requisitos Técnicos](#requisitos-técnicos)
* [Modelo do Domínio](#Modelo-do-Domínio)
* [Arquitetura da Solução](#arquitetura-da-solução)
* [Tecnologias](#tecnologias)
* [Planificação](#planificação)
* [Interface](#interface)
* [Conclusão](#conclusão)
* [Bibliografia](#bibliografia)

## Elementos do Grupo:

* 20230331 - David Bação
* 202301001 - Francisco Lourinho
* 20241805 - Vivandro Kambaza
* 20230853 - Yosvany Nunes

## Poster

![Poster do Projeto](posteriadego.png)

## Palavras-Chave

Bar; Comida; IADE; Universidade; QR Code; Reservas; Salas; GPS; Infraestrutura; Mobile; App; Interativo; Experiência do Estudante; Refeitório; Localizador.

## Descrição


IADEGO é uma app mobile voltada para o ambiente académico IADE com foco principal em:

- Reservas de lanches em tempo real (evitar filas).  
- Geração de QR Code para levantar o pedido no bar (QR válido por 10 minutos).  
- GPS interno para localizar salas e pontos de interesse do campus.
   * Visualização e marcação de salas como livres/ocupadas.
   * Reservar salas de aulas para estudo.

### Motivação

Sabemos que o tempo e algo que não podemos alterar, baseado nessa ideia tentamos criar alguma forma de otimizar algo de forma a que os utilizadores possam ter uma experiencia melhor com a nossa app. Com este pensamento e apos termos tido várias conversas com professores sobre melhorias na infraestrutura, pensamos numa app para o IADE - Universidade Europeia. Nessa app poderíamos ter varias vertentes como reserva de salas, mapas internos ou tempos de espera em filas, que podem dispender de vários minutos da nossa vida, causando frustração nos estudantes. Essa ideia foi entretanto explorada e falada com mais pormenor chegando a ideia original atual.

### Objetivos

* Melhorar a experiência do estudante no campus IADE.
	* Localização - Guiar o aluno para o lugar da universidade que desejar
	* Comida - Disponibilizar reservas rápidas de comida e salas.
	* Tempo
* Integrar controle ambiental (luz/ar condicionado) de forma simples.
* Incentivar feedback dos utilizadores para manter dados sempre atualizados.

## Público-Alvo

* Estudantes e professores do **IADE** (uso interno da infraestrutura).

## Pesquisa de Mercado


Não encontramos nenhuma aplicação que reúna todas as funcionalidades do **IADE GO**, embora existam soluções que oferecem partes isoladas dessas funcionalidades.

-   **Uber Eats, Bolt Food, Glovo:** Estas aplicações focam-se na entrega de comida, permitindo ao utilizador fazer o pedido diretamente na app e escolher entre levantar o pedido na loja ou recebê-lo em casa.
    
-   **Tablets Kiosk digitais dos centros comerciais:** Apesar de não estarem disponíveis ao público em geral, estes dispositivos utilizam uma tecnologia que orienta os utilizadores de loja em loja dentro de um centro comercial.

## Guiões de Teste

1. **Reservar comida no bar.**
	* Abrir app **IADE GO**
	* Fazer **login** com a sua conta estudante
	* Clicar em **Cantina/Bar**
	* Escolher o que deseja.
	* Clicar no icone do **Carrinho de compras**
	* **Concluir** a sua compra.
	* Escolher o **método de pagamento**.
	* **Recolher** o lanche em 10 minutos. (depois de concluído)
2. **Seguir rota GPS até à sala da próxima aula.**
	* Abrir app **IADE GO**
	* Fazer **login** com a sua conta estudante
	* Clicar em **Mapa IADE**
	* **Escolher** o lugar onde deseja ir.
	* Escolher opção **Começar**.
3. **Reservar sala vazia.**
	* Abrir app **IADE GO**
	* Fazer **login** com a sua conta estudante
	* Clicar em **Mapa IADE**
	* **Escolher** a sala onde deseja ir.
	* **Escolher** reservar sala.

## Plano de Trabalho

**Fases principais (milestones):**

1.  **Planeamento e Proposta**  (semana 1–2)
    -   Definição do escopo, pesquisa, mockups iniciais (Figma), diagrama de domínio.
2.  **Protótipo (MVP)**  (semanas 3–6)
    -   App Android: UI de menu, reserva, QR display.
    -   Backend básico: endpoints reserva, geração QR.
    -   BD com dados fictícios.
3.  **Desenvolvimento / Integração**  (semanas 7–10)
    -   GPS interno, notificações, fluxo bar (validação QR).
    -   Documentação REST, diagrama de classes.
4.  **Testes e Ajustes**  (semanas 11–12)
    -   Testes unitários, integração, correções.
5.  **Entrega Final**  (semana 13)
    -   Relatório, poster, vídeo promo, apresentação.

Planeamos o projeto usando a ferramenta abaixo. Esta ferramenta foi usada para a gestão de de todas as tarefas e subtarefas onde usamos listas e gráficos de gantt, para nos organizarmos.

https://sharing.clickup.com/90151674235/l/h/6-901515651075-1/eb8431e1a265761

## Descrição da Solução e Enquadramento das Unidades Curriculares 

* **Programação de Dispositivos Móveis:** Android em Kotlin. * App em Kotlin.
* **POO:** Java. * Backend em Java.
* **Bases de Dados:** MySQL. * Base de dados MySQL.
* **Matemática Discreta:** lógica de rotas, reservas e algoritmos internos.
* **Projeto de Desenvolvimento Móvel:** planeamento e gestão.
* **Competências Comunicacionais:** Posters e Apresentações.

## Requisitos Técnicos

### **Funcionais**

1.  **Gestão de salas**  
    -   Ver disponibilidade das salas de aula em tempo real, com base nos horários.
    -   Possibilidade de marcar salas como “livres” (zonas verdes).
    -   Orientação por GPS/Indoor Maps até a sala da próxima aula.
2.  **Controle de infraestrutura (IoT)**
      -   Conectar via **Bluetooth** ou rede local para ligar/desligar luzes e ar condicionado.      
4.  **Gestão de refeições (bar/cantina)**
    -   Reservar comida através da aplicação.
    -   Possibilidade de pagamento in-app (cartão/MBWay) ou em pessoa.
    -   Confirmação da reserva e retirada no bar.
5.  **Autenticação e segurança**
    -   Login com credenciais institucionais (ex.: email IADE).
    -   Possibilidade de integrar cartão de estudante digital (QR ou NFC).

### **Não Funcionais**
1.  **Usabilidade**
    -   Interface simples e intuitiva para estudantes.
    -   Compatível com Android (idealmente também iOS no futuro).
2.  **Performance**
    -   Resposta em tempo real para reservas e disponibilidade.
    -   Baixo consumo de bateria, especialmente no uso de GPS/Bluetooth.
3.  **Confiabilidade**
    -   O sistema deve funcionar mesmo com falhas temporárias de internet (ex.: cache local).
    -   Garantir consistência nas reservas (evitar duplicações).
4.  **Segurança**
    -   Dados dos estudantes protegidos (GDPR).
    -   Pagamentos seguros e encriptados.
5.  **Escalabilidade**
    -   Possibilidade de expansão para outros campi ou universidades.
    -   Estrutura modular (ex.: fácil adicionar novas funções, como biblioteca ou estacionamento).
6.  **Integração**
    -   Compatível com os sistemas já existentes do IADE (ex.: horários, autenticação).        
    -   Uso de APIs e padrões abertos (REST, OAuth, etc.).

## Modelo do Domínio

**Entidades principais:**

-   **User (Aluno)**: id, nome, email , curso, turma.
-   **Product (Produto do Bar)**: id, nome, descrição, preço, disponibilidade.
-   **Order (Pedido)**: id, user_id, estado (reservado / pronto / levantado), total, created_at.
-   **OrderItem**: id, order_id, product_id, quantity, price.
-   **QRCode**: id, order_id, code_value, issued_at, expires_at, used_flag.
-   **Room (Sala/Local)**: id, nome, piso.
-   **Notification**: id, user_id, type, message, 'created_at, read_flag.


![Modelo de Domínio](modelodedominio.png)

(Relacionamentos: User 1…* Orders; Order 1…* OrderItems; Order 1…1 QRCode; Room usada para GPS/rotas.)

## Arquitetura da Solução

* **Front-end:** App Android em Kotlin/Compose.
* **Back-end:** Spring Boot (REST APIs).
* **DB:** MySQL.
* **Integrações:** QR Code, GPS interno, Bluetooth.

## Tecnologias

* Kotlin + Jetpack Compose.
* Java + Spring Boot.
* MySQL.
* Figma.

## Planificação

[ClickUp][[(https://sharing.clickup.com/90151674235/gr/h/2kyqarbv-175/98c212984e03221>)](https://clickup.com/)](https://app.clickup.com/t/86c5nymvw)

## Interface

Inclui telas de:

* Menu;
* Mapa IADE;
* Menu Cafetaria;
* Menu de informação;
* Perfil

## Conclusão

O projeto **IADE GO** visa otimizar tempo dos estudantes, beneficiando aqueles que usam a app, evitando filas, menos stress, reserva de lanches e salas. Esta app embora seja projetada mais para estudantes, pode ser usada pelos professores e para visitas inesperadas ao IADE.

## Bibliografia

* [Portal-IADE](https://portalestudante.iade.pt/login-ws?mId=loginIADE&eId=show)
* [IADE](https://www.iade.europeia.pt/)
* [Kotlin](https://kotlinlang.org)
* [Jetpack Compose](https://developer.android.com/compose)
* [Spring Boot](https://spring.io)
* [MySQL](https://www.mysql.com/)
* [Figma](https://www.figma.com/)
