<?php
require_once "html/header.php";
require_once "classes/Inscricao.php";
require_once "classes/Eventos.php";

$apiEventos = new Eventos();
$dataEventos = $apiEventos->getEventos();
$eventos = $dataEventos['eventos'] ?? [];

$eventosInscritos = [];

if (isset($_SESSION['usuario']['id'])) {
    $inscricao = new Inscricao();
    $inscricoes_usuario = $inscricao->getInscricoesUsuario($_SESSION['usuario']['id']);
    $eventosInscritos = array_map(function ($insc) {
        return $insc['evento_id'];
    }, $inscricoes_usuario);
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_SESSION['usuario']['id'], $_POST['evento_id'])) {

    $usuario_id = $_SESSION['usuario']['id'];
    $evento_id = $_POST['evento_id'];

    $inscricao = new Inscricao();
    $response = $inscricao->criarInscricao($usuario_id, $evento_id);

    $inscricoes_usuario = $inscricao->getInscricoesUsuario($usuario_id);

    // Cria um array só com os IDs dos eventos já inscritos
    $eventosInscritos = array_map(function ($insc) {
        return $insc['evento_id'];
    }, $inscricoes_usuario);

    if (isset($response['errors']) && is_array($response['errors'])) {

        foreach ($response['errors'] as $erro_obj) {

            if (isset($erro_obj['message'])) {
                $erros[] = $erro_obj['message'];
            }
        }
    }
}
?>

<body>
    <?php if (isset($_SESSION['usuario'])): ?>
        <?php if (empty($eventosInscritos)): ?>
            <div class="container">
                <div class="row">
                    <div class="col-12 mb-5 mt-5 d-flex justify-content-center align-items-center"
                        style="height: 400px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                        <h2 style="color: #FFFFFF;"><i class="bi bi-folder2-open"></i> Não tem nenhuma inscrição sua ainda.</h2>
                    </div>
                </div>
            </div>
        <?php else: ?>
            <div class="container">
                <div class="row gx-3">
                    <?php foreach ($eventos as $evento): ?>
                        <?php if (in_array($evento['id'], $eventosInscritos)): ?>
                            <div class="col-3">
                                <div class="mb-5 mt-5 d-flex"
                                    style="height: 450px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                                    <div class="card w-100">
                                        <img src="../<?= htmlspecialchars($evento['foto_url']) ?>" class="card-img-top"
                                            alt="<?= htmlspecialchars($evento['nome']) ?>" style="object-fit: cover; height: 230px;">
                                        <div class="card-body">
                                            <h5 class="card-title"><?= htmlspecialchars($evento['nome']) ?></h5>
                                            <p class="card-text">
                                                <i class="bi bi-map"></i> - <?= htmlspecialchars($evento['endereco']) ?><br>
                                                <i class="bi bi-stopwatch"></i> - <?= htmlspecialchars($evento['hora']) ?><br>
                                                <i class="bi bi-megaphone"></i> -
                                                <?= htmlspecialchars($evento['nome_palestrante']) ?><br>
                                                <i class="bi bi-journal-bookmark"></i> -
                                                <?= htmlspecialchars($evento['nome_curso']) ?><br>
                                            </p>
                                            <?php if (in_array($evento['id'], $eventosInscritos)): ?>
                                                <button class="btn btn-secondary mt-auto" disabled>
                                                    Já inscrito
                                                </button>
                                            <?php else: ?>
                                                <button onclick="abrirModal('<?= htmlspecialchars($evento['id']) ?>')"
                                                    class="btn btnInscrever mt-auto">
                                                    Inscrever-se
                                                </button>
                                            <?php endif; ?>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <?php endif; ?>
                    <?php endforeach ?>

                </div>
            </div>

        <?php endif ?>
    <?php else: ?>
        <div class="container">
            <div class="row">
                <div class="col-12 mb-5 mt-5 d-flex justify-content-center align-items-center"
                    style="height: 400px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                    <h2 style="color: #FFFFFF;"><i class="bi bi-person"></i> Faça <a href="login.php"
                            style="color: #FFFFFF;">login</a> para ter acesso.</h2>
                </div>
            </div>
        </div>
    <?php endif ?>

    <!-- <?php if (in_array($evento['id'], $eventosInscritos)): ?>
                <div class="row mb-3 mt-3" id="card-inscrito">
                    <div class="col-3">
                        <img id="img-card-certificado" src="../<?= htmlspecialchars($evento['foto_url']) ?>"
                            alt="<?= htmlspecialchars($evento['nome']) ?>">
                    </div>
                    <div class="col-6">
                        <h2><?= htmlspecialchars($evento['nome']) ?></h2>
                        <p>
                            <i class="bi bi-map"></i> <?= htmlspecialchars($evento['endereco']) ?><br>
                            <i class="bi bi-clock"></i> <?= htmlspecialchars($evento['hora']) ?><br>
                            <i class="bi bi-person"></i> <?= htmlspecialchars($evento['nome_palestrante']) ?><br>
                        </p>
                    </div>
                    <div class="col-3 d-flex align-items-center justify-content-center">

                        <button class="btn" id="btn-pendente"><i class="bi bi-watch"></i> Pendente</button>
                        <button class="btn" id="btn-certificado"><i class="bi bi-printer"></i> Aprovado</button>
                    </div>
                </div>
            <?php endif ?> -->

</body>
<?php
require_once "html/footer.php";
?>