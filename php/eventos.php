<?php
require_once "html/header.php";
require_once "classes/Eventos.php";

$api = new Eventos();
$data = $api->getEventos();

$eventos = $data['eventos'] ?? [];

require_once "html/header.php";

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

                <div class="container">
                    <div class="row gx-3">

                        <?php foreach ($eventos as $evento): ?>
                            <div class="col-3">
                                <div class="mb-5 mt-5 d-flex"
                                    style="height: 450px; background-color:rgb(75, 113, 128); border-radius: 16px;">
                                    <div class="card w-100">
                                        <img src="../<?= htmlspecialchars($evento['foto_url']) ?>" class="card-img-top"
                                            alt="<?= htmlspecialchars($evento['nome']) ?>"
                                            style="object-fit: cover; height: 230px;">
                                        <div class="card-body">
                                            <h5 class="card-title"><?= htmlspecialchars($evento['nome']) ?></h5>
                                            <p class="card-text">
                                                <i class="bi bi-map"></i> - <?= htmlspecialchars($evento['endereco']) ?><br>
                                                <i class="bi bi-stopwatch"></i> - <?= htmlspecialchars($evento['hora']) ?><br>
                                                <i class="bi bi-megaphone"></i> - <?= htmlspecialchars($evento['nome_palestrante']), htmlspecialchars($evento['palestrante_id'])?><br>
                                                <i class="bi bi-journal-bookmark"></i> - <?= htmlspecialchars($evento['nome_curso']), htmlspecialchars($evento['curso_id']) ?><br> 
                                            </p>
                                            <a href="#" class="btn btnInscrever">Inscrever-se</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <?php endforeach ?>

                    </div>
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


</body>
<?php
require_once "html/footer.php";
?>