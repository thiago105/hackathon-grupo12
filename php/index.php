<?php
session_start();
require_once 'classes/Eventos.php';

$api = new Eventos();
$data = $api->getEventos();

$eventos = $data['eventos'] ?? [];

require_once "html/header.php";
?>

<body>

    <div class="container mb-5 mt-3">
        <div class="row">
            <div class="col-12 col-md-8 offset-md-2">
                <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="true">
                    <div class="carousel-inner">
                        <?php if (empty($eventos)): ?>
                            <div class="container d-flex justify-content-center align-items-center" id="erro-msg">
                                <h3>Não há eventos disponíveis.</h3>
                            </div>
                        <?php else:
                            $contador = 0;
                            shuffle($eventos);
                            foreach ($eventos as $evento): ?>
                                <div class="carousel-item <?= $contador === 0 ? 'active' : '' ?>">
                                    <img src="<?= htmlspecialchars($evento['foto_url']) ?>" class="d-block w-100"
                                        alt="<?= htmlspecialchars($evento['nome']) ?>">
                                    <div class="carousel-caption d-none d-md-block">
                                        <h5><?= htmlspecialchars($evento['nome']) ?></h5>
                                    </div>
                                </div>
                                <?php
                                $contador++;
                                if ($contador >= 4)
                                    break;
                            endforeach;
                        endif; ?>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying"
                        data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying"
                        data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
        </div>
    </div>

</body>
<?php
require_once "html/footer.php";
?>