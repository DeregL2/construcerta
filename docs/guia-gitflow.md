# Guia de Gitflow — ConstruCerta

Este guia traz o passo a passo para publicar o projeto no GitHub seguindo
o fluxo de gitflow pedido pelo professor: branch `main`, branch `develop`,
branches `feature/*` e Pull Request.

Rode os comandos abaixo no PowerShell, dentro da pasta do projeto.

## 1. Crie o repositório vazio no GitHub

No seu GitHub pessoal, crie um repositório novo chamado `construcerta`
(ou outro nome de sua preferência), **público**, sem README/gitignore
automático (o projeto já traz os seus).

## 2. Inicialize o git localmente e configure o remoto

```powershell
git init
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/construcerta.git
```

## 3. Primeiro commit (estrutura inicial) direto na `main`

```powershell
git add .
git commit -m "estrutura inicial do ConstruCerta (login seguro, Spring Boot + MongoDB Atlas)"
git push -u origin main
```

## 4. Crie a branch `develop`

```powershell
git checkout -b develop
git push -u origin develop
```

## 5. A partir daqui, use branches de feature

Para qualquer ajuste futuro (correções, novas telas, documentação etc.),
sempre parta de `develop`:

```powershell
git checkout develop
git pull origin develop
git checkout -b feature/nome-da-feature
```

Exemplo, para a documentação:

```powershell
git checkout -b feature/documentacao-abnt
git add docs/documentacao-construcerta.pdf docs/guia-gitflow.md
git commit -m "docs: adiciona documentacao tecnica em PDF (ABNT) e guia de gitflow"
git push -u origin feature/documentacao-abnt
```

## 6. Abra o Pull Request

No GitHub, abra um Pull Request da branch `feature/*` para `develop`.
Preencha um título e descrição curtos e confirme a criação do PR.

## 7. Faça o merge do PR

Depois de revisar, clique em **Merge pull request** para levar as
mudanças para `develop`.

## 8. Atualize a `main` com o conteúdo final

```powershell
git checkout main
git pull origin main
git merge develop
git push origin main
```

## 9. Confirme que o repositório está público

Em **Settings > General > Danger Zone** do repositório no GitHub,
confirme que a visibilidade está como **Public** (é um dos requisitos
da atividade).

---

### Resumo do fluxo usado neste repositório

- `main` — versão estável/entregue do projeto.
- `develop` — integração das features antes de ir para `main`.
- `feature/*` — uma branch por funcionalidade/entrega, sempre partindo de
  `develop` e voltando para `develop` via Pull Request.

Esse é o mesmo fluxo (`feature/` + Pull Request) usado nas atividades
semanais do curso Java Spring Fundamentals.
