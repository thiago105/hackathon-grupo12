<?php
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

        <?php endif ?>
        <div class="container">
            <div class="row">
                <div class="col12"></div>
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