<?php
require_once "html/header.php";
require_once "classes/Eventos.php";
require_once "classes/Inscricao.php";

$erros = [];

$apiEventos = new Eventos();
$dataEventos = $apiEventos->getEventos();

$eventos = $dataEventos['eventos'] ?? [];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['curso_id']) && $_POST['curso_id'] !== '') {
    $cursoFiltrado = $_POST['curso_id'];
    $eventos = array_filter($eventos, function ($evento) use ($cursoFiltrado) {
        return $evento['curso_id'] == $cursoFiltrado;
    });
}

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
        <?php if (empty($eventos)): ?>
            <div class="container">
                <div class="row">
                    <div class="col-12 mb-5 mt-5 d-flex justify-content-center align-items-center"
                        style="height: 400px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                        <h2 style="color: #FFFFFF;"><i class="bi bi-box2"></i> Não há eventos no momento.</h2>
                    </div>
                </div>
            </div>
        <?php else: ?>

            <div class="container">
                <div class="row mt-3">
                    <div class="col-11"></div>
                    <div class="col-1 ">
                        <div class="dropdown-container">
                            <button class="dropdown-button">
                                <i class="bi bi-funnel"></i> Filtros
                            </button>

                            <div class="dropdown-content">
                                <form method="post">
                                    <label for="curso_id" style="color:black;">Filtrar por curso:</label>
                                    <select name="curso_id" id="curso_id" class="form-select mb-2">
                                        <option value="">-- Todos os cursos --</option>
                                        <?php
                                        $cursosUnicos = [];
                                        foreach ($eventos as $evento) {
                                            $cursoNome = $evento['nome_curso'];
                                            $cursoId = $evento['curso_id'];
                                            if (!isset($cursosUnicos[$cursoId])) {
                                                $cursosUnicos[$cursoId] = $cursoNome;
                                                echo "<option value=\"$cursoId\">" . htmlspecialchars($cursoNome) . "</option>";
                                            }
                                        }
                                        ?>
                                    </select>
                                    <button type="submit" class="btn btn-primary mt-2">Aplicar filtro</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row gx-3">
                    <?php foreach ($eventos as $evento): ?>
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
                    <?php endforeach ?>

                </div>
            </div>
        <?php endif ?>
        <div class="container">
            <div class="row">
                <div class="col-12"></div>
            </div>
        </div>
    <?php else: ?>
        <div class="container">
            <div class="row">
                <div class="col-12 mb-5 mt-5 d-flex justify-content-center align-items-center"
                    style="height: 400px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                    <h2 style="color: #FFFFFF;"><i class="bi bi-person"></i> Faça login para ter acesso.</h2>
                </div>
            </div>
        </div>
    <?php endif ?>
    <div>
    </div>
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
    <script>
        function abrirModal(evento_id) {
            document.getElementById('modalInscricao').style.display = 'flex';
            document.getElementById('evento_id').value = evento_id;
        }
        function fecharModal() {
            document.getElementById('modalInscricao').style.display = 'none';
        }

        document.getElementById('btnFiltar').addEventListener('click', function () {
            const filtroDiv = document.getElementById('filtroCurso');
            filtroDiv.style.display = (filtroDiv.style.display === 'none') ? 'block' : 'none';
        })
    </script>
</body>
<?php
require_once "html/footer.php";
?>