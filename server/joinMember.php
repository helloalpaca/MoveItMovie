<?php

  $mysql_hostname = 'localhost';
  $mysql_username = 'root';
  $mysql_password = '123456';
  $mysql_database = 'moveitmovie';

  $connect = mysqli_connect($mysql_hostname,$mysql_username,$mysql_password,$mysql_database);

  mysqli_select_db($connect,$mysql_database) or die('DB선택 실패');

  $userid = isset($_POST['id']) ? $_POST['id'] : '';
  $userpw = isset($_POST['pw']) ? $_POST['pw'] : '';

  if($userid !="" and $userpw !=""){
    $sql = "insert into user(id,pw) values('$userid','$userpw')";
    $result = mysqli_query($connect,$sql);

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
         user pw: <input type = "text" name = "pw" />
         <input type = "submit" />
    </form>
  </body>
  </html>
  <?php
  }
?>
