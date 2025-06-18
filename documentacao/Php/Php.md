# DOCUMENTAÇÃO PHP

Sistema desenvolvido em PHP para gerenciamento de eventos acadêmicos, incluindo usuários, cursos, inscrições, certificados e interface visual.

---

## 📦 CLASSES PRINCIPAIS (`php/classes/`)

### `ApiService.php` *(opcional)*
- **Função:** Comunicação com APIs externas (caso aplicável).
- **Métodos esperados:**
  - `get()` – Buscar dados de uma API.
  - `post()` – Enviar dados para uma API.

### `Certificado.php`
- **Função:** Geração de certificados em PDF para usuários aprovados em eventos.
- **Métodos esperados:**
  - `gerarCertificado($usuarioId, $eventoId)` – Cria e disponibiliza o certificado em PDF com dados do evento e do usuário.

### `Curso.php`
- **Função:** Gerenciamento dos cursos disponíveis.
- **Métodos esperados:**
  - `listarCursos()` – Retorna todos os cursos cadastrados.
  - `cadastrarCurso($nome)` – Adiciona um novo curso ao banco de dados.

### `Eventos.php`
- **Função:** Controle de eventos (CRUD).
- **Métodos esperados:**
  - `listarEventos()` – Retorna todos os eventos.
  - `buscarEvento($id)` – Retorna os dados de um evento específico.
  - `cadastrarEvento($dados)` – Insere um novo evento.

### `Inscricao.php`
- **Função:** Gerenciamento de inscrições de usuários em eventos.
- **Métodos esperados:**
  - `inscrever($usuarioId, $eventoId)` – Registra nova inscrição.
  - `verificarInscricao($usuarioId, $eventoId)` – Verifica se o usuário já está inscrito.

### `Usuario.php`
- **Função:** Autenticação e gerenciamento de usuários.
- **Métodos esperados:**
  - `login($email, $senha)` – Valida credenciais e inicia sessão.
  - `cadastrar($dados)` – Registra novo usuário.
  - `atualizarPerfil($id, $dados)` – Edita informações do usuário.

---

## 📄 PÁGINAS PRINCIPAIS (`php/`)
| Página           | Descrição                                                                 |
|------------------|---------------------------------------------------------------------------|
| `index.php`      | Página inicial, exibe eventos e informações gerais.                       |
| `login.php`      | Formulário de login, redireciona para o perfil ou lista de eventos.       |
| `cadastrar.php`  | Formulário de cadastro de novos usuários.                                 |
| `eventos.php`    | Lista todos os eventos disponíveis.                                       |
| `inscricao.php`  | Permite que usuários se inscrevam em eventos.                             |
| `perfil.php`     | Exibe e permite edição do perfil do usuário logado.                       |
| `logout.php`     | Encerra a sessão do usuário.                                              |
| `sobre.php`      | Informações sobre o sistema (descrição, objetivos, contato).              |

---

## 🎨 FRONT-END (`css/`, `html/`, `images/`, `uploads/`)
### `style.css`
- Estilização global (cores, fontes, espaçamento, responsividade).

### `header.php` & `footer.php`
- `header.php` – Barra de navegação (login, cadastro, perfil).
- `footer.php` – Rodapé com links úteis e informações institucionais.

### `images/` & `uploads/`
- `images/` – Logotipos, ícones e imagens estáticas.
- `uploads/` – Imagens enviadas por usuários (ex: perfil, banners de eventos).

---

## 🔁 FLUXOS PRINCIPAIS
### 1. **Cadastro de Usuário**
- `cadastrar.php → Usuario.php`
- Validação de dados → Registro no banco → Redirecionamento para login.

### 2. **Login**
- `login.php → Usuario.php`
- Verificação de e-mail/senha → Início de sessão → Redirecionamento para perfil/eventos.

### 3. **Inscrição em Evento**
- `inscricao.php → Inscricao.php`
- Verifica se o usuário já está inscrito → Registra no banco → Exibe mensagem de confirmação.

### 4. **Geração de Certificado**
- `Certificado.php`
- Busca dados do evento e do usuário → Gera PDF → Disponibiliza para download.

---

📌 **Observação final:** O sistema utiliza programação orientada a objetos em PHP, seguindo boas práticas de organização de código e separação de responsabilidades por classe. Pode ser facilmente expandido para incluir funcionalidades como upload de certificados, painel administrativo e notificações por e-mail.
