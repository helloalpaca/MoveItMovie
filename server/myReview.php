<?php

  $mysql_hostname = 'localhost';
  $mysql_username = 'root';
  $mysql_password = '123456';
  $mysql_database = 'moveitmovie';

  $connect = mysqli_connect($mysql_hostname,$mysql_username,$mysql_password,$mysql_database);

  mysqli_select_db($connect,$mysql_database) or die('DB선택 실패');

  $id = isset($_POST['id']) ? $_POST['id'] : '';
  $android = strpos($_SERVER['HTTP_USER_AGENT'],"Android");

  if($id!=""){
  	$sql = "select * from theaterReviews where id = '$id'";
    $sql_result = mysqli_query($connect,$sql); //sql문 실행
    $data = array();

    if($sql_result){
    while($info=mysqli_fetch_array($sql_result)){
      array_push($data, array('theater'=>$info['theater'],'screen'=>$info['screen'],'kind'=>$info['kind'],'taste'=>$info['taste'],'clean'=>$info['clean'],'FreeReview'=>$info['FreeReview']));
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
         user 이름: <input type = "text" name = "id" />
         <input type = "submit" />
    </form>
  </body>
  </html>
  <?php
  }
?>
