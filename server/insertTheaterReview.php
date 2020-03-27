<?php

  $mysql_hostname = 'localhost';
  $mysql_username = 'root';
  $mysql_password = '123456';
  $mysql_database = 'moveitmovie';

  $connect = mysqli_connect($mysql_hostname,$mysql_username,$mysql_password,$mysql_database);

  mysqli_select_db($connect,$mysql_database) or die('DB선택 실패');

  $userid = isset($_POST['id']) ? $_POST['id'] : '';
  $theaterName = isset($_POST['theater']) ? $_POST['theater'] : '';
  $screen2 = isset($_POST['screen']) ? $_POST['screen'] : '';
  $kind2 = isset($_POST['kind']) ? $_POST['kind'] : '';
  $taste2 = isset($_POST['taste']) ? $_POST['taste'] : '';
  $clean2 = isset($_POST['clean']) ? $_POST['clean'] : '';
  $review = isset($_POST['FreeReview']) ? $_POST['FreeReview'] : '';

  $screen = (int)$screen2;
  $kind = (int)$kind2;
  $taste = (int)$taste2;
  $clean = (int)$clean2;

  if(true){
    $query = "Update theaterList Set screen = screen+$screen, kind=kind+$kind,taste=taste+$taste, clean=clean+$clean, totalReview=totalReview+1 where theater = '$theaterName'";

    $sql = "insert into theaterReviews(id,theater,screen,kind,taste,clean,FreeReview) values('$userid','$theaterName','$screen','$kind','$taste','$clean','$review')";
    $result = mysqli_query($connect,$sql);

    //$query = "insert into theaterList(theater,screen,kind,taste,clean) values('$theaterName','$screen','$kind','$taste','$clean')";

    $query_result = mysqli_query($connect,$query);

    if($query_result){
      echo "query 성공";
    }
    else{
      echo "query 실패";
      echo mysqli_error($connect);
    }

    if($result){
      echo "sql 성공";
    }
    else{
      echo "sql 실패";
      echo mysqli_error($connect);
    }

  }
  else{
    echo "데이터를 입력하세요";
  }

  mysqli_close($connect);
?>

<?php

  $android = strpos($_SERVER['HTTP_USER_AGENT'],"Android");
  if(!$android){
?>
  <html>
  <body>
    <form action="<?php $_PHP_SELF ?>" method="POST">
         user id: <input type = "text" name = "id" />
         theater Name: <input type = "text" name = "theater" />
         screen: <input type = "number" name = "screen" />
         kind: <input type = "number" name = "kind" />
         popcorn: <input type = "number" name = "taste" />
         clean: <input type = "number" name = "clean" />
         Free Review: <input type = "text" name = "FreeReview" />
         <input type = "submit" />
    </form>
  </body>
  </html>
  <?php
  }
?>
