<?php
$connection = mysqli_connect ("localhost", "root", "", "crud");
    
$id='';
  $id = (isset($_POST['id'])? $_POST['id']: '');

// $id =$_POST["id"];
$sql = "DELETE FROM cerita_table WHERE id='$id'";

$result = mysqli_query($connection, $sql);

if ($result){
    echo "Data Deleted";
}else{
     echo "Failed";
}

mysqli_close($connection); 

?>