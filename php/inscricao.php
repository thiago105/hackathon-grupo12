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
                        <h2 style="color: #FFFFFF;"><i class="bi bi-box2"></i> Não há eventos no momento.</h2>
                    </div>
                </div>
            </div>
        <?php else: ?>
            
        <?php endif ?>
    <?php else: ?>
        <!-- aviso sem login -->
        <div class="container">
            <div class="row">
                <div class="col-12 mb-5 mt-5 d-flex justify-content-center align-items-center"
                    style="height: 400px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                    <h2 style="color: #FFFFFF;"><i class="bi bi-person"></i> Faça login para ter acesso.</h2>
                </div>
            </div>
        </div>
    <?php endif ?>
    <div id="modalInscricao" class="modal">
        <div class="container w-50" id="modal">
            <form method="post">
                <div class="container">
                    <div class="row">
                        <input type="hidden" id="evento_id" name="evento_id">
                        <div class="col-12 mt-3 d-flex align-items-center justify-content-center">
                            <h4>Tem certeza de que quer se inscrever neste evento?</h4>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6 d-flex align-items-center justify-content-center"><button type="button"
                                class="btn" id="btn-falha" onclick="fecharModal()">Cancelar</button></div>
                        <div class="col-6 d-flex align-items-center justify-content-center"><button type="submit"
                                class="btn" name="atualizar" id="btn-sucesso">Confirmar</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

        <div class="container">
                <?php 
                $contador = 0;
                foreach ($eventos as $evento): ?>
                    <?php if (in_array($evento['id'], $eventosInscritos)): ?>
                        <div class="row mb-3 mt-3" id="card-inscrito">
                            <div class="col-3" >
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
                                
                                <button class="btn" id="btn-pendente"><i class="bi bi-watch"></i>  Pendente</button>
                                <button class="btn" id="btn-certificado"><i class="bi bi-printer"></i>  Aprovado</button>
                            </div>
                        </div>
                    <?php endif ?>
                <?php 
                $contador++;
                if($contador > 1){break;}
                endforeach ?>
            </div>
</body>
<?php
require_once "html/footer.php";
?>