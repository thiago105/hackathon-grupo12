<?php
session_start();
require_once 'classes/Usuario.php';

if (isset($_SESSION['usuario']['id'])) {
    
    $idUsuarioLogado = $_SESSION['usuario']['id'];

    $apiUsuario = new Usuario();
    $dadosUsuario = $apiUsuario->listarUsuario($idUsuarioLogado);
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
<header>
    <div class="row" id="navBar">
        <div class="col-1" id="logo"><a href="index.php"><img src="images/logoUnialfa.png" alt="logo da Unialfa"
                    width="180" height="67"></a></div>
        <div class="col-10 d-flex justify-content-center align-items-center">
            <nav class="d-flex justify-content-center align-items-center">
                <ul>
                    <li><a href="inscricao.php">Inscrições</a></li>
                    <li><a href="eventos.php">Eventos</a></li>
                    <li><a href="sobre.php">Sobre</a></li>
                </ul>
            </nav>
        </div>
        <div class="col-1 d-flex justify-content-center align-items-center">
            <?php if(isset($_SESSION['usuario'])):?>
                <a href="#"><img id="foto_perfil" src="<?= htmlspecialchars($dadosUsuario["foto_url"]) ?>" alt="<?= htmlspecialchars($dadosUsuario["nome"]) ?>"></a>
            <?php else:?>
               <a href="login.php"><button type="button" class="btn"id="btnLogin"><i class="bi bi-person"></i>Login</button></a> 
            <?php endif?>    
        </div>
</header>