# 📘 DOCUMENTAÇÃO NODE.JS (API BACK-END)

Este projeto é uma API REST desenvolvida em **Node.js** com **TypeScript** para gerenciamento de eventos acadêmicos. O sistema permite o cadastro e controle de **usuários, cursos, eventos, palestrantes, inscrições** e geração de **certificados em PDF**.

---

## 🗄️ ESTRUTURA DO BANCO DE DADOS (MIGRATIONS)
### Tabela: `cursos`
- Armazena informações sobre os cursos oferecidos.
- **Campos:** `id`, `nome`, `created_at`, `updated_at`

### Tabela: `palestrantes`
- Contém os dados dos palestrantes dos eventos.
- **Campos:** `id`, `nome`, `mini_curriculo`, `tema`, `foto_url`, `created_at`, `updated_at`

### Tabela: `eventos`
- Registra os eventos acadêmicos disponíveis.
- **Campos:** `id`, `nome`, `data_inicio`, `hora`, `endereco`, `foto_url`, `curso_id`, `palestrante_id`, `created_at`, `updated_at`

### Tabela: `usuarios`
- Armazena dados dos usuários (alunos/professores).
- **Campos:** `id`, `foto_url`, `nome`, `email` (único), `senha_hash`, `curso_id`, `created_at`, `updated_at`

### Tabela: `inscricoes`
- Controla as inscrições de usuários em eventos.
- **Campos:** `id`, `usuario_id`, `evento_id`, `data_inscricao`, `aprovado` (boolean), `created_at`, `updated_at`
- **Restrição:** `UNIQUE (usuario_id, evento_id)` para evitar duplicações.

---

## 🌐 ROTAS PRINCIPAIS
### 🔒 `autenticacao.ts`
- Middleware de autenticação via JWT.
- Valida o cabeçalho Authorization no formato `Bearer token`.
- **Atenção:** o segredo JWT está hardcoded (deveria ser variável de ambiente).

### 📄 `certificado.ts`
- Gera certificados em PDF para usuários aprovados.
- Realiza JOIN entre tabelas de usuários, eventos, palestrantes e cursos.
- Utiliza a biblioteca **PDFKit** para criar o certificado.

### 🎓 `cursos.ts`
- `GET /cursos` – Lista todos os cursos.

### 📅 `eventos.ts`
- `GET /eventos` – Lista eventos com nomes de cursos e palestrantes (joins).
- `GET /eventos/:id` – Retorna dados de um evento específico.

### ✅ `inscricoes.ts`
- `GET /inscricoes/usuario/:usuario_id` – Lista inscrições de um usuário.
- `GET /inscricoes` – Lista todas as inscrições com detalhes.
- `POST /inscricoes` – Cria nova inscrição (valida duplicidade).

### 🔐 `login.ts`
- `POST /login` – Realiza autenticação de usuário com `email/senha`.
- Utiliza **bcrypt** para verificação da senha.
- Retorna token JWT válido por **5 horas**.

### 🎤 `palestrantes.ts`
- `GET /palestrantes/:id` – Retorna dados de um palestrante específico.

### 👤 `usuarios.ts`
- `GET /usuarios` – Lista todos os usuários.
- `GET /usuarios/:id` – Retorna dados de um usuário específico.
- `POST /usuarios` – Cria novo usuário (valida e-mail único e aplica hash).
- `PUT /usuarios/:id` – Atualiza dados do usuário (foto, nome ou senha).

---

## ⚙️ CONFIGURAÇÕES E DEPENDÊNCIAS
### `server.ts`
- Arquivo principal de inicialização do servidor Express.
- Middlewares: CORS, JSON parsing.
- Tratamento de erros com validações usando **Zod**.
- Porta padrão: `3000`.

### `knexfile.js`
- Configurações do **Knex** para ambiente `development`.
- Conecta ao banco MySQL com `host`, `user`, `password`, `database`.
- Define diretórios de **migrations** e **seeds**.

### `package.json`
- **Principais dependências:**
  - `express`, `knex`, `mysql2`
  - `bcrypt` – Hash de senhas
  - `jsonwebtoken` – Autenticação via token JWT
  - `pdfkit` – Geração de certificados
  - `zod` – Validação de schemas
- **Scripts úteis:** Migrations (`knex migrate:latest`, etc.)

---

## 🔁 FLUXOS IMPORTANTES
### 1. Cadastro de Usuário
- Recebe dados → Valida com `Zod` → Verifica e-mail único → Gera hash da senha com `bcrypt` → Insere no banco.

### 2. Login
- Recebe `email` e `senha` → Compara com hash → Retorna token JWT válido.

### 3. Inscrição em Evento
- Verifica duplicidade → Cria nova inscrição com `usuario_id` e `evento_id`.

### 4. Geração de Certificado
- Verifica se inscrição foi aprovada → Realiza joins com dados do evento → Gera certificado em PDF com layout personalizado.

---

📌 **Observação final:** O projeto segue boas práticas de organização de rotas, middleware, e separação de responsabilidade. Pode ser facilmente adaptado para produção com ajustes em variáveis de ambiente, segurança e versionamento.
