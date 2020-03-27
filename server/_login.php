<?php

  $mysql_hostname = 'localhost';
  $mysql_username = 'root';
  $mysql_password = '123456';
  $mysql_database = 'moveitmovie';

  $connect = mysqli_connect($mysql_hostname,$mysql_username,$mysql_password,$mysql_database);

  mysqli_select_db($connect,$mysql_database) or die('DB선택 실패');

  extract($_POST);

  $sql = "select * from user";
  $sql_result = mysqli_query($connect,$sql); //sql문 실행
  $data =array();

  if($sql_result){
    while($info=mysqli_fetch_array($sql_result)){
      array_push($data, array('id'=>$info['id'],'pw'=>$info['pw']));
    }
  }

  header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("users"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;


  mysqli_close($connect);
?>
