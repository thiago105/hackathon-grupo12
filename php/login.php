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
                    <label for="email" class="form-label">E-mail:</label>
                    <input type="email" class="form-control border-dark" id="email" name="email"
                        aria-describedby="emailHelp" required>
                    <div id="emailHelp" class="form-text">Nunca passe o email para ninguém</div>
                </div>
                <div class="mb-3">
                    <label for="senha" class="form-label">Senha:</label>
                    <input type="password" class="form-control border-dark" id="senha" name="senha" required>
                </div>
                <pre class="erro-login"> </pre>
                <div class="cointainer-btn">
                    <button type="submit" class="btn" id="btnLgn">Logar</button>
                </div>
                <div class="cointainer-btn mt-3">
                    <p><a href="#">Esqueceu a senha?</a> | <a href="cadastrar.php">Criar conta.</a></p>
                </div>
            </div>
        </form>
    </div>

</body>

</html>