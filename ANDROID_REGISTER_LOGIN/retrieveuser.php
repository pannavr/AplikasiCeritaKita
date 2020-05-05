<?php
require_once 'connect.php';

$result = array();
$result['data'] = array();
$select = "SELECT *from user_table";

$responce = mysqli_query($conn, $select);

while($row = mysqli_fetch_array($responce))
{

    $index['id'] = $row['0'];
    $index['name'] = $row['1'];
    $index['email'] = $row['2'];
    $index['password'] = $row['3'];
    $index['photo'] = $row['4'];

    array_push($result['data'], $index);

}

$result['success']="1";

echo json_encode($result);
mysqli_close($conn); 


?>
