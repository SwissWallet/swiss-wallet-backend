# SwissWallet

Bem-vindo ao Projeto SwissWallet! Aqui você encontrará informações importantes e links úteis.



# Descrição do projeto
Swiss Wallet

Projeto desenvolvido por alunos do SENAI Suíço Brasileiro, a Swiss Wallet é uma carteira digital destinada a promover a importância da AAPM e forcener ajuda a ela na administração de atividades sociais em busca de ajudar os alunos.

A Swiss Wallet permite alunos com baixa renda solicitar um auxílio para suprir suas necessidades de estudos, podendo assim dar o seu melhor em sala de aula. Na nossa carteira digital é possível flexibilizar pontos com o objetivo de se adaptar a necessidade do aluno. Dentre elas transporte, alimentação dentro da instituição, estudos, entre outras.

Além disso, a SwissWallet possibilita a arrecadação de verba para AAPM com a compra de pontos e doações, ela possue uma store com itens exclusivos que são adquiridos apenas via app. Os alunos podem trocar seus pontos por combos AAPM na cantina com preços acessíveis, e também ganhar bolsas em cursos como recompensa pela sua contribuição a AAPM.

# Repositórios do projeto 

| Stack               | Link                                      |
|--------------------|-------------------------------------------|
| Back-End    | https://github.com/SwissWallet/swiss-wallet-backend  |
| Web       | https://github.com/SwissWallet/swiss-wallet-web |
| Mobile  | https://github.com/SwissWallet/swiss-wallet-mobile |

# Git Flow - Guia de Trabalho

Este documento descreve o fluxo de trabalho de Git adotado por nossa equipe para garantir um desenvolvimento eficiente e organizado. O fluxo de trabalho Git Flow ajuda a gerenciar ramificações (branches) e a integração contínua do código.

## Estrutura de Branches

1. **`Developer`**
   - A branch `develop` é usada para o desenvolvimento de novas funcionalidades e correções de bugs.
   - Os merges nesta branch representam o código que está pronto para ser integrado e testado na branch de `QA`.

2. **`QA (Quality Assurance)`**
   - Usadas para validar o código e garantir que ele atende aos padrões de qualidade antes do lançamento final.
   - Criadas a partir da branch developer para testes de qualidade e integração.
   - **Objetivo**: Realizar testes de QA completos e garantir que todas as funcionalidades estejam funcionando conforme esperado e sem novos problemas.
  
4. **`Master`**
   - Esta é a branch principal onde o código de produção está sempre em um estado estável e pronto para ser lançado.
   - Todos os merges na branch `master` devem ser estáveis e testados.

3. **Branches de Funcionalidades (Feature Branches)**
   - **Nome**: `feature/nome-da-funcionalidade`
   - Criadas a partir da branch `develop` para o desenvolvimento de novas funcionalidades.
   - Após a conclusão do desenvolvimento, deve-se fazer um pull request para a branch `develop`.
   - Exemplo de criação: `git checkout -b feature/nome-da-funcionalidade develop`

4. **Branches de Correções (Hotfix Branches)**
   - **Nome**: `hotfix/nome-da-correção`
   - Criadas a partir da branch `main` para correção de bugs críticos em produção.
   - Após a correção, deve-se fazer um pull request para a branch `main` e `develop`.
   - Exemplo de criação: `git checkout -b hotfix/nome-da-correção main`

5. **Outras Branches**
   - **Também é possível a utilização de outras branches semânticas de acordo com a necessidade da alteração que esta branch representa.**

## Fluxo de Trabalho

1. **Início de uma Nova Funcionalidade**
   - Atualize a branch `develop`
   - Crie uma branch de funcionalidade a partir de `developer`.
   - Trabalhe na funcionalidade e faça commits frequentemente.
   - Quando a funcionalidade estiver concluída, abra um pull request para `developer` e solicite uma revisão de código.

3. **Preparando um Novo Lançamento**
   - Solicitar um merge para a branch `qas` após rigorosos testes para testar se o código está e conformidade e identificar possíveis falhas de segurança.
   - Caso não o código não passar nos teste em **QA** se deve realizar as alterações necessárias nas sua branch de funcionalidade e repetir todo o processo.
   - Quando estiver pronto, abra um pull request para `master`.

4. **Correções Críticas em Produção**
   - Crie uma branch de hotfix a partir de `master`.
   - Corrija o problema e faça commits.
   - Quando a correção estiver pronta, abra um pull request para `master` e `developer`.

5. **Atualização de Branches**
   - Mantenha sua branch atualizada com `developer` para evitar conflitos.
   - Antes de criar um pull request, faça um merge de `developer` na sua branch de funcionalidade, release ou hotfix.

## Comandos Úteis

- **Atualizar sua branch atual**:
  ```bash
  git pull origin <nome_da_branch_atual>

- **Criar e mudar para uma nova branch de funcionalidade**:
  ```bash
  git checkout -b feature/nome-da-funcionalidade develop

- **Subir seu código para o repositório remoto**:
  ```bash
  git add .
  git commit -m "<semântica>: <descrição>"
  git push origin <nome_da_branch_atual>

**Solicitar pull request via GitHub com base nos padrões definidos.**

<p align="center">
  <img src="docs/images/bordao.png" alt="Tela do Projeto" width="600" />
</p>
