<?php

require_once 'classes/Usuario.php';
require_once 'classes/Curso.php';

$erros = [];

$apiCursos = new Curso();
$data = $apiCursos->getCursos();
$cursos = $data['cursos'] ?? [];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit'])) {

    $nome = $_POST['nome'];
    $email = $_POST['email'];
    $senha_hash = $_POST['senha'];
    $curso_id = $_POST['curso_id'];

    $usuario = new Usuario();
    $response = $usuario->cadastrar($nome, $email, $senha_hash, $curso_id);

    if (isset($response['errors'])) {
        $erros = $response['errors'];
    }

    if (empty($erros)) {
        header("location: ./login.php");
        exit;
    }
}
?>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unialfa eventos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous"
        defer>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO" crossorigin="anonymous"
        defer></script>
    <link rel="stylesheet" href="css/style.css">
    <link rel="icon" type="image/png" href="images/favicon.png">
</head>

<body>

    <div class="container col-4">

        <div id="logo" class="d-flex justify-content-center align-items-center mt-5 mb-3">
            <img src="images/logoUnialfa.png" alt="logo da Unialfa" width="412" height="150">
        </div>
        <form method="POST">

            <div>
                <div class="mb-3">
                    <label for="nome" class="form-label">Nome:</label>
                    <input type="text" class="form-control border-dark" id="nome" name="nome" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">E-mail:</label>
                    <input type="email" class="form-control border-dark" id="email" name="email"
                        aria-describedby="emailHelp" required>
                </div>
                <div class="mb-3">
                    <label for="senha" class="form-label">Senha:</label>
                    <input type="password" class="form-control border-dark" id="senha" name="senha" required>
                </div>
                <div class="mb-3">
                    <label for="curso_id" class="form-label">Selecione um Curso:</label>
                    <select class="form-select border-dark" id="curso_id" name="curso_id" required>
                        <?php if (empty($cursos)) { ?>
                            <option value="" selected disabled>Sem cursos cadastrados</option>
                        <?php } else { ?>
                            <option value="" selected disabled>Clique para selecionar um curso</option>
                            <?php foreach ($cursos as $curso) { ?>
                                <option value="<?= htmlspecialchars($curso['id']) ?>">
                                    <?= htmlspecialchars($curso['nome']) ?>
                                </option>
                            <?php } ?>
                        <?php } ?>
                    </select>
                </div>
                <div class="cointainer-btn">
                    <button type="submit" class="btn" id="btnLgn">Casdastrar</button>
                </div>
            </div>
        </form>
    </div>

</body>

</html>