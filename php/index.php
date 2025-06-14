<?php
    require_once 'classes/ApiService.php';

    $api = new ApiService();
    $gets = $api->getUsers();

    require_once "html/header.php";
?>

<body>

 <div class="container mb-5 mt-3">
        <div class="row">
            <div class="col-12 col-md-8 offset-md-2"> 
                <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="true">
                    <div class="carousel-inner">
                        <?php if (empty($gets)): ?>
                            <p>Não foi possível carregar os eventos</p>
                        <?php else: ?>  
                        <?php foreach($gets as $get): ?>      
                        <div class="carousel-item active">
                            <img src="<?= htmlspecialchars($get['foto_url'])?>" class="d-block w-100" alt="<?= htmlspecialchars($get['nome'])?>">
                        </div>
                        <?php 
                            endforeach;
                            endif;
                        ?>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
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