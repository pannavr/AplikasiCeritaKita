<?php

$connection = mysqli_connect ("localhost", "root", "", "crud");

$id = (isset($_POST['id'])? $_POST['id']: '');
$judul = (isset($_POST['judul'])? $_POST['judul']: '');
$lokasi = (isset($_POST['lokasi'])? $_POST['lokasi']: '');
$lat = (isset($_POST['lat'])? $_POST['lat']: '');
$lon = (isset($_POST['lon'])? $_POST['lon']: '');
$cerita = (isset($_POST['cerita'])? $_POST['cerita']: '');

$sql = "UPDATE cerita_table SET judul = '$judul', lokasi = '$lokasi', cerita = '$cerita', lat = '$lat', lon ='$lon' WHERE id = '$id'";
     
$result = mysqli_query($connection, $sql);

if($result){
    echo "Data Update";
}else{
    echo "Failed";
}

mysqli_close($connection);
?>