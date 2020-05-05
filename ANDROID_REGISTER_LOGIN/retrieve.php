<?php
require_once 'connect.php';

$result = array();
$result['data'] = array();
$select = "SELECT *from cerita_table";

$responce = mysqli_query($conn, $select);

while($row = mysqli_fetch_array($responce))
{

    $index['id'] = $row['0'];
    $index['judul'] = $row['1'];
    $index['lat'] = $row['2'];
    $index['lon'] = $row['3'];
    $index['lokasi'] = $row['4'];
    $index['cerita'] = $row['5'];

    array_push($result['data'], $index);

}

$result['success']="1";

echo json_encode($result);
mysqli_close($conn); 


?>
