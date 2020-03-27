<?php

  $mysql_hostname = 'localhost';
  $mysql_username = 'root';
  $mysql_password = '123456';
  $mysql_database = 'moveitmovie';

  $connect = mysqli_connect($mysql_hostname,$mysql_username,$mysql_password,$mysql_database);

  mysqli_select_db($connect,$mysql_database) or die('DB선택 실패');

  $theater = isset($_POST['theater']) ? $_POST['theater'] : '';
  $android = strpos($_SERVER['HTTP_USER_AGENT'],"Android");

  if($theater !=""){
  	$sql = "select * from theaterList where theater = '$theater'";
    $sql_result = mysqli_query($connect,$sql); //sql문 실행
    $data = array();

    if($sql_result){
    while($info=mysqli_fetch_array($sql_result)){
      array_push($data, array('screen'=>$info['screen'],'kind'=>$info['kind'],'taste'=>$info['taste'],'clean'=>$info['clean'],'totalReview'=>$info['totalReview']));
    }
  }

  header('Content-Type: application/json; charset=utf8');
  $json = json_encode(array("theaters"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
  echo $json;
  }

  mysqli_close($connect);
?>

<?php

  $android = strpos($_SERVER['HTTP_USER_AGENT'],"Android");
  if(!$android){
?>

  <!DOCTYPE html>
  <html>
  <body>
    <form action="<?php $_PHP_SELF ?>" method="POST">
         영화관 이름: <input type = "text" name = "theater" />
         <input type = "submit" />
    </form>
  </body>
  </html>
  <?php
  }
?>
