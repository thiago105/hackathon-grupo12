# 🎓 Sistema de Gestão de Eventos – UniALFA

Sistema completo para gestão de eventos acadêmicos da UniALFA, composto por três aplicações integradas:

- **🔧 Backend (Node.js):** API RESTful para gerenciamento de dados (cursos, eventos, palestrantes, usuários e inscrições)
- **🌐 Frontend Web (PHP):** Site público para visualização de eventos e inscrições
- **🖥️ Aplicação Desktop (Java):** Ferramenta administrativa para cadastro e gestão avançada

🎯 **Objetivo:** Centralizar e automatizar a organização de eventos acadêmicos, desde o cadastro até a emissão de certificados digitais.

---

## 👥 Equipe de Desenvolvimento
- Eduardo Henrique Pereira Dos Santos
- Thiago Vinicius Santos da Silva
- Gabriel Henrique Friedrichsen
- Hendreu Satosh Zampieri Itami
- Andre Mateus Roll      

---

## 🛠️ Tecnologias Utilizadas

### Backend (Node.js)
- Express.js + Knex.js
- Autenticação via JWT
- Geração de certificados com PDFKit

### Frontend Web (PHP)
- PHP com MySQLi
- HTML/CSS
- Upload de imagens (eventos e perfis)

### Aplicação Desktop (Java)
- Java Swing (interface gráfica)
- Conexão com MySQL via JDBC

### Banco de Dados
- MySQL 8.0
- Tabelas relacionais normalizadas: cursos, eventos, usuários, inscrições, palestrantes

---

## 🗂️ Estrutura do Sistema

### 📦 Backend – `Node.js`
📂 src/
├── routes/           → Rotas da API (eventos, cursos, usuários)
├── middlewares/      → Autenticação JWT
├── db/               → Migrations e queries (Knex)
└── server.ts         → Configuração do servidor

### 🌐 Frontend Web – `PHP`
📂 PHP/
├── CLASSES/          → Lógica PHP (Eventos, Usuários)
├── CSS/              → Estilos
├── UPLOADS/          → Imagens de eventos/perfis
└── index.php         → Página inicial

### 🖥️ Desktop – `Java (Swing)`
📂 src/main/java/grupo12/
├── DAO/              → Acesso ao banco de dados
├── GUI/              → Telas Swing
├── MODEL/            → Entidades (Eventos, Cursos)
└── SERVICE/          → Regras de negócio

---

## 🚀 Funcionalidades Implementadas
| Módulo          | Descrição                                                                   |
|-----------------|-----------------------------------------------------------------------------|
| **Cadastro**    | Inclusão de cursos, palestrantes, eventos e usuários, com validações        |
| **Inscrições**  | Usuários se inscrevem via PHP, aprovação gerenciada pela aplicação Java     |
| **Certificados**| Emissão automática em PDF para participantes aprovados (via Node.js)        |
| **Gestão**      | Controle de presença, verificação de conflitos e relatórios administrativos |

---

## ⚙️ Como Executar
### Backend (Node.js)
- Instale as dependências:
`npm install`
- Configure o banco em knexfile.js.

Execute:
`bash`
`npm run dev`

### Frontend Web (PHP)
- Hospede os arquivos em um servidor Apache com PHP 7.4+**
- Configure a conexão MySQL em CLASSES/**

### Desktop (Java)
- Importe o projeto Maven**
- Ajuste as credenciais do MySQL em `Dao.java`

**Execute `Main.java`**

## 📄 Licença
- Projeto desenvolvido para o Hackathon UniALFA.

## Equipe GRUPO 12 – 2025 🚀