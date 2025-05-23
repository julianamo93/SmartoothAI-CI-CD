# 🦷 Smartooth AI 🦷

### API Java SpringBoot com gradle utilizada para deploy no webapp da Azure com banco de dados Azure SQL

![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)

## ✨ Sobre

O Smartooth AI é um sistema desenvolvido em Java com o framework Spring Boot, focado na otimização de serviços odontológicos utilizando inteligência artificial e machine learning. O sistema visa oferecer uma experiência personalizada aos usuários, facilitando a escolha de procedimentos odontológicos e destacando planos de saúde disponíveis. Além disso, conta com um programa de pontos que incentiva a fidelização dos pacientes.

Neste repositório encontra-se a API responsável pelo gerenciamento de profissionais, pacientes, planos, e demais funcionalidades voltadas para a área odontológica.


## 🪐 Developers 

- [Juliana Moreira](https://github.com/julianamo93) - Modelagem de Dados e Cloud - RM554113 - 2TDSPR
- [Kevin Nobre](https://github.com/KevinNobre) - Backend e Front - RM552590 - 2TDSZ
- [Sabrina Couto](https://github.com/sabrinacouto) - Backend Developer - RM552728 - 2TDSZ
  

## Modelo DER
![Captura de tela 2025-03-20 011231](https://github.com/user-attachments/assets/36285977-ae30-4380-9130-78dac38aca5b)


## Diagrama de Classes

![Captura de tela 2025-03-19 231027](https://github.com/user-attachments/assets/3a23b5c3-2b11-4f0e-ae2b-79eaebf75e56)

## Arquitetura da Solução

[Smartooth-Archi.pdf](https://github.com/user-attachments/files/19358853/Smartooth-Archi.pdf)

## Vídeo da Solução

[https://youtu.be/2XcoVLipeoc](https://www.youtube.com/watch?v=xa1YPsNZ9Xw)

## Pipeline YAML CI/CD Azure

Pipeline YAML CI/CD criado com Azure DevOps; Foi criada uma infraestrutura, build e deploy da aplicação java com .JAR, 
além da criação de um banco de dados Azure SQL Database para inserção e persistência de dados na nuvem, validados via Postman.

### Etapas do Pipeline
🔁 Trigger
Acionada automaticamente em pushs para a branch main.

🛠️ 1. Criar Infraestrutura (Stage: criarInfra)
Azure CLI é utilizado para:
- Criar o Resource Group.
- Criar o App Service Plan Linux (F1 – gratuito).
- Criar o Web App com runtime Java 17.

🔨 2. Build da Aplicação (Stage: BuildApp)
- Compila o projeto usando o Gradle.
- Lista arquivos para verificação.
Copia o .jar gerado e publica como artefato.

🚀 3. Deploy da Aplicação (Stage: deployApp)
- Faz o download do artefato publicado.
- Lista os arquivos para conferência.
Usa o AzureRmWebAppDeployment para fazer deploy no Azure Web App.

### Pipeline
```bash
  trigger:
  branches:
    include:
      - main

pool:
  vmImage: 'ubuntu-latest'

variables:
  rm: rm554113
  location: eastus
  resourceGroup: rg-smartoothai
  servicePlan: smartoothAi
  appName: smartooth-Ai-$(rm)
  runtime: JAVA|17-java17
  sku: F1
  nome_artefato: smartoothai

  SPRING_DATASOURCE_URL: 'jdbc:sqlserver://svr-dbsprint4.database.windows.net:1433;databaseName=db-sprint4;encrypt=true;trustServerCertificate=false;loginTimeout=30;'
  SPRING_DATASOURCE_USERNAME: 'USER'
  SPRING_DATASOURCE_PASSWORD: 'PASSWORD'
  SPRING_JPA_HIBERNATE_DDL_AUTO: 'none'

stages:
  - stage: criarInfra
    jobs:
      - job: criaWebApp
        displayName: 'Criar o Serviço de Aplicativo'
        steps:
          - task: AzureCLI@2
            inputs:
              azureSubscription: 'MyAzureSubscription'
              scriptType: 'bash'
              scriptLocation: 'inlineScript'
              inlineScript: |
                az group create --location $(location) --name $(resourceGroup)
                az appservice plan create -g $(resourceGroup) -n $(servicePlan) --is-linux --sku F1
                az webapp create -g $(resourceGroup) -p $(servicePlan) -n $(appName) --runtime "$(runtime)"
            displayName: 'Criar Resource Group, App Service Plan e Web App'

  - stage: BuildApp
    variables:
      gradleWrapperFile: 'gradlew'
    jobs:
      - job: buildApp
        displayName: 'Realizar build do App'
        steps:
          - task: Gradle@3
            displayName: 'Build SmartoothAi'
            inputs:
              gradleWrapperFile: '$(gradleWrapperFile)'
              tasks: 'build'
              testRunTitle: 'Testes Unitários'
              jdkVersionOption: 1.17

          - script: ls -lR $(System.DefaultWorkingDirectory)
            displayName: 'Listar conteúdo da pasta base (System.DefaultWorkingDirectory)'

          - task: CopyFiles@2
            inputs:
              SourceFolder: '$(System.DefaultWorkingDirectory)/build/libs'
              Contents: 'SmartoothAI-0.0.1.jar'
              TargetFolder: '$(Build.ArtifactStagingDirectory)'

          - task: PublishBuildArtifacts@1
            displayName: 'Publicar artefato do Build SmartoothAi'
            inputs:
              PathtoPublish: '$(Build.ArtifactStagingDirectory)'
              ArtifactName: $(nome_artefato)

  - stage: deployApp
    jobs:
      - job: deployWebApp
        displayName: 'Deploy no Serviço de Aplicativo'
        steps:
          - task: DownloadBuildArtifacts@1
            displayName: 'Baixar artefato gerado'
            inputs:
              buildType: 'current'
              downloadType: 'specific'
              artifactName: $(nome_artefato)
              downloadPath: '$(System.DefaultWorkingDirectory)'

          - script: ls -R $(System.DefaultWorkingDirectory)/$(nome_artefato)
            displayName: 'Listar arquivos do artefato baixado'

          - task: AzureRmWebAppDeployment@4
            displayName: 'Deploy SmartoothAi'
            inputs:
              azureSubscription: 'MyAzureSubscription'
              appType: 'webAppLinux'
              WebAppName: $(appName)
              packageForLinux: '$(System.DefaultWorkingDirectory)/$(nome_artefato)/SmartoothAI-0.0.1.jar'
```
![image](https://github.com/user-attachments/assets/668a7c70-420e-4b02-b742-48b4c96f88b9)


## 💬 Rodando localmente

### Clone o projeto

```bash
  git clone https://github.com/sabrinacouto/SmartoothAI-API.git
```

### Configurar o Projeto no IntelliJ IDEA
<ul>
  <li>Selecione a opção "Open" e navegue até o diretório do projeto Smartooth AI clonado.</li>
  <li>Em Project Structure garanta que o SDK esteja para o Java 17.</li>
  <li>Verifique no IntelliJ se a aba do Gradle está presente na barra lateral.</li>
  <li>Caso o projeto não seja automaticamente reconhecido como Gradle, abra o arquivo build.gradle e aceite a importação do Gradle quando o IntelliJ perguntar.</li>
</ul>

### Configurar o JDBC para o Oracle Database
Configurar as credenciais do banco de dados Oracle no application.properties:
  ```bash
  spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```


### Rodando a Aplicação

Vá até a classe principal que possui a anotação @SpringBootApplication e clique no botão Run ou Debug no IntelliJ.


Utilize Postman ou Insomnia para realizar
as requisições das rotas com a URL:

```endpoint
   http://localhost:8080/
```
## 📌 Endpoints para o UsuarioPaciente

| Método  | Endpoint                           | Descrição |
|---------|------------------------------------|-----------|
| `POST`  | `/usuario/cadastro`               | Processa o cadastro de um novo usuário. |
| `GET`   | `/usuario/editarUsuario/{id}`     | Exibe os dados do formulário de edição do usuário. |
| `PATCH` | `/usuario/editarUsuario/{id}`     | Atualiza os dados do usuário. |
| `DELETE`| `/usuario/deletarUsuario/{id}`    | Exclui um usuário do sistema. |
| `GET`   | `/usuario/logout`                 | Encerra a sessão do usuário logado. |


## 📌 Endpoints para o Plano


| Método  | Endpoint                | Descrição |
|---------|-------------------------|-----------|
| `POST`  | `/planos/cadastro`      | Processa o cadastro de um novo plano. |
| `GET`   | `/planos/{id}/editar`   | Exibe o formulário de edição do plano. |
| `PUT`   | `/planos/{id}/editar`   | Atualiza os dados de um plano. |
| `DELETE`| `/planos/{id}/excluir`  | Exclui um plano do sistema. |


## 👩‍💻 Exemplo de teste
 
![CadastroLoginJava](https://github.com/user-attachments/assets/3a49ad66-68d5-4904-8a0c-b4ef54c418b1)


## 📍 Dependências
```gradle
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```
