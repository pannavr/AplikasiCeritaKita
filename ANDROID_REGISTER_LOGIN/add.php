<?php
   
    
if ($_SERVER['REQUEST_METHOD'] == 'POST' ){

$judul = $_POST['judul'];
$lokasi = $_POST['lokasi'];
$lat = $_POST['lat'];
$lon = $_POST['lon'];
$cerita = $_POST['cerita'];


require_once 'connect.php';

    $sql = "INSERT INTO cerita_table (judul, lokasi, cerita, lat, lon) VALUES ('$judul', '$lokasi', '$cerita', '$lon', '$lat')";

    if ( mysqli_query($conn, $sql)) {
        $result['success'] = "1";
        $result['message'] = "success";

        echo json_encode($result);
        mysqli_close($conn);
        }else {

            $result["success"] = "0";
            $result["message"] = "error";

            echo json_encode($result);
            mysqli_close($conn);
        }

}

?>