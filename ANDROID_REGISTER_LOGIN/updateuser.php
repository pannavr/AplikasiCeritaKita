<?php

$connection = mysqli_connect ("localhost", "root", "", "crud");

$id = (isset($_POST['id'])? $_POST['id']: '');
$name = (isset($_POST['name'])? $_POST['name']: '');
$email = (isset($_POST['email'])? $_POST['email']: '');
$password = (isset($_POST['password'])? $_POST['password']: '');
$photo = (isset($_POST['photo'])? $_POST['photo']: '');

$sql = "UPDATE user_table SET name = '$name', email = '$email', password = '$password', photo ='$photo'  WHERE id = '$id'";
     
$result = mysqli_query($connection, $sql);

if($result){
    echo "Data Update";
}else{
    echo "Failed";
}

mysqli_close($connection);
?>