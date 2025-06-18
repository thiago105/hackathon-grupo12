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

    foreach ($inscricoes_usuario as $insc) {
        $eventosInscritos[$insc['evento_id']] = [
            'aprovado' => $insc['aprovado'],
            'inscricao_id' => $insc['id']
        ];
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_SESSION['usuario']['id'], $_POST['inscricao_id'])) {
    $inscricao_id = $_POST['inscricao_id'];

    $inscricao = new Inscricao();
    $response = $inscricao->apagarInscricao($inscricao_id);
    echo "<script>console.log('inscricao_id recebido: " . $inscricao_id . "');</script>";
    if (!isset($response['errors'])) {
        header('Location: ' . $_SERVER['PHP_SELF']);
        exit();
    }

    $erros = [];
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
                    <?php foreach ($eventos as $evento):
                        $horarioSemSegundos = substr($evento['hora'], 0, 5);
                        $dateTime = new DateTime($evento['data_inicio']);
                        $fuso_horario_brasil = new DateTimeZone('America/Sao_Paulo');
                        $dateTime->setTimezone($fuso_horario_brasil);
                        $data_formatada = $dateTime->format('d/m/Y');
                        ?>
                        <?php if (isset($eventosInscritos[$evento['id']])): ?>
                            <div class="col-3">
                                <div class="mb-5 mt-5 d-flex"
                                    style="height: 450px; background-color:rgb(75, 113, 128); border-radius: 16px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.5)">
                                    <div class="card w-100 ">
                                        <img src="../Java/java/<?= htmlspecialchars($evento['foto_url']) ?>" class="card-img-top"
                                            alt="<?= htmlspecialchars($evento['nome']) ?>" style="object-fit: cover; height: 230px;">
                                        <div class="card-body">
                                            <h5 class="card-title"><?= htmlspecialchars($evento['nome']) ?></h5>
                                            <p class="card-text">
                                                <i class="bi bi-geo-alt-fill"></i></i> - <?= htmlspecialchars($evento['endereco']) ?><br>
                                                <i class="bi bi-calendar-date"></i> - <?= htmlspecialchars($data_formatada) ?> -
                                                <?= htmlspecialchars($horarioSemSegundos) ?><br>
                                                <i class="bi bi-megaphone"></i> -
                                                <?= htmlspecialchars($evento['nome_palestrante']) ?><br>
                                                <i class="bi bi-journal-bookmark"></i> -
                                                <?= htmlspecialchars($evento['nome_curso']) ?><br>
                                            </p>
                                            <div class="container">
                                                <div class="row">
                                                    <?php if ($eventosInscritos[$evento['id']]['aprovado'] == 0): ?>
                                                        <div class="col-6">
                                                            <button class="btn" id="btn-pendente" disabled>
                                                                <i class="bi bi-watch"></i> Pendente.
                                                            </button>
                                                        </div>
                                                        <div class="col-6">
                                                            <button class="btn btn-cancelar"
                                                                onclick="abrirCancelar(<?= $eventosInscritos[$evento['id']]['inscricao_id'] ?>)">
                                                                <i class="bi bi-x-circle"></i> Cancelar
                                                            </button>
                                                        </div>
                                                    <?php else: ?>
                                                        <div class="col-12 d-flex justify-content-center align-items-center">
                                                            <button
                                                                onclick="abrirImprimir(<?= $_SESSION['usuario']['id'] ?>, <?= $evento['id'] ?>)"
                                                                class="btn btnInscrever mt-auto">
                                                                <i class="bi bi-printer"></i> Imprimir.
                                                            </button>
                                                        </div>
                                                    <?php endif; ?>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <?php endif; ?>
                    <?php endforeach; ?>
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

    <div id="modalInscricao" class="modal" style="display: none;">
        <div class="container w-50" id="modal">
            <form method="POST">
                <input type="hidden" id="inscricao_id" name="inscricao_id">
                <div class="container">
                    <div class="row">
                        <div class="col-12 mt-3 d-flex align-items-center justify-content-center">
                            <h4>Tem certeza de que quer cancelar a participação neste evento?</h4>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-6 d-flex align-items-center justify-content-center">
                            <button type="button" class="btn" id="btn-falha" onclick="fecharModal()">Cancelar</button>
                        </div>
                        <div class="col-6 d-flex align-items-center justify-content-center">
                            <button type="submit" class="btn" id="btn-sucesso">Confirmar</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    </div>

    <script>
        function abrirCancelar(inscricao_id) {
            console.log("Cancelar inscrição com ID:", inscricao_id); // ← DEBUG
            document.getElementById('modalInscricao').style.display = 'flex';
            document.getElementById('inscricao_id').value = inscricao_id;
        }

        function fecharModal() {
            document.getElementById('modalInscricao').style.display = 'none';
        }
        function abrirImprimir(usuarioId, eventoId) {
            const url = `http://localhost:3001/certificado/${usuarioId}/${eventoId}`;   
            window.open(url, '_blank');
        }
    </script>

</body>
<?php require_once "html/footer.php"; ?>