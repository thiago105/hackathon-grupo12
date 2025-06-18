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
    
    // Modificado: Armazena todos os dados da inscrição, incluindo o ID
    foreach ($inscricoes_usuario as $insc) {
        $eventosInscritos[$insc['evento_id']] = [
            'aprovado' => $insc['aprovado'],
            'inscricao_id' => $insc['id'] // Assumindo que a tabela tem coluna 'id'
        ];
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_SESSION['usuario']['id'], $_POST['evento_id'])) {
    $usuario_id = $_SESSION['usuario']['id'];
    $evento_id = $_POST['evento_id'];

    $inscricao = new Inscricao();
    $response = $inscricao->criarInscricao($usuario_id, $evento_id);

    $inscricoes_usuario = $inscricao->getInscricoesUsuario($usuario_id);

    // Atualizado para o novo formato
    $eventosInscritos = [];
    foreach ($inscricoes_usuario as $insc) {
        $eventosInscritos[$insc['evento_id']] = [
            'aprovado' => $insc['aprovado'],
            'inscricao_id' => $insc['id']
        ];
    }

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
                                style="height: 450px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                                <div class="card w-100">
                                    <img src="../<?= htmlspecialchars($evento['foto_url']) ?>" class="card-img-top"
                                        alt="<?= htmlspecialchars($evento['nome']) ?>" style="object-fit: cover; height: 230px;">
                                    <div class="card-body">
                                        <h5 class="card-title"><?= htmlspecialchars($evento['nome']) ?></h5>
                                        <p class="card-text">
                                            <i class="bi bi-map"></i> - <?= htmlspecialchars($evento['endereco']) ?><br>
                                            <i class="bi bi-calendar-date"></i> - <?= htmlspecialchars($data_formatada) ?> -
                                            <?= htmlspecialchars($horarioSemSegundos) ?><br>
                                            <i class="bi bi-megaphone"></i> - <?= htmlspecialchars($evento['nome_palestrante']) ?><br>
                                            <i class="bi bi-journal-bookmark"></i> - <?= htmlspecialchars($evento['nome_curso']) ?><br>
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
                                                                onclick="abrirCancelar(<?= htmlspecialchars($eventosInscritos[$evento['id']]['inscricao_id']) ?>)">
                                                            <i class="bi bi-x-circle"></i> Cancelar
                                                        </button>
                                                    </div>
                                                <?php else: ?>
                                                    <div class="col-12 d-flex justify-content-center align-items-center">
                                                        <button onclick="abrirModal('')"
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
</body>
<?php require_once "html/footer.php"; ?>