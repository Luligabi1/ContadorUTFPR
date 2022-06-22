# Instruções para execução do Bot


1. Crie uma conta no [Twitter Developers](https://developer.twitter.com), seu projeto e consiga o [Acesso elevado](https://developer.twitter.com/en/docs/twitter-api/getting-started/about-twitter-api);
2. Clone esse repositório;
3. Crie um arquivo chamado `twitter4j.properties` na root do projeto com o seguinte conteúdo:

```
oauth.consumerKey=
oauth.consumerSecret=
oauth.accessToken=
oauth.accessTokenSecret=
```
Preencha as lacunas com os token gerados para seu projeto no Twitter Developers. **Certifique-se que seus tokens possuem acesso de escrita**, se não o bot não será capaz de postar!

4. Compile o .jar do projeto utulizando o comando `gradlew build`;
5. Na mesma pasta onde o .jar ficará, crie um arquivo `data.properties`:

```
semesterStartDate=2022-03-01
semesterEndDate=2022-12-31
currentSemester=2022.1
universityName=UTFPR
timeZone=UTC-3
```

Este arquivo define as datas, semestre atual, nome da universidade e fuso horário utilizados pelo bot; certifique-se de reiniciar o bot depois de alterar esses dados.

6. Rode o .jar e pronto! O bot postará um tweet por dia, começando no momento que é iniciado e então a cada 24 horas.
