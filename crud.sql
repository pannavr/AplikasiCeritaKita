-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 05 Bulan Mei 2020 pada 06.03
-- Versi server: 10.1.30-MariaDB
-- Versi PHP: 7.2.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `crud`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `cerita_table`
--

CREATE TABLE `cerita_table` (
  `id` int(11) NOT NULL,
  `judul` varchar(50) NOT NULL,
  `lat` text NOT NULL,
  `lon` text NOT NULL,
  `lokasi` text NOT NULL,
  `cerita` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `cerita_table`
--

INSERT INTO `cerita_table` (`id`, `judul`, `lat`, `lon`, `lokasi`, `cerita`) VALUES
(50, 'judul pertama', '110.5075799', '-7.2754171', '05/04/2020', '05/04/2020');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_table`
--

CREATE TABLE `user_table` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` text NOT NULL,
  `photo` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `user_table`
--

INSERT INTO `user_table` (`id`, `name`, `email`, `password`, `photo`) VALUES
(28, '12', '12', '$2y$10$ShI9xrQBeNgQMxQFJZJRneq4kloaITbQys0r2RB.V55Gp8NabLmYu', ''),
(30, '22', '22', '$2y$10$krJDJy6lEfUlMTORp8oGYek9rM4Wg.0PinD38bZewEq1tBJQnrjwm', ''),
(38, 'Nopa', 'nopa@gmail.com', '$2y$10$GIl35mKkDQs/crcOzKYg2.GsR4F5AsLbhlIabGD9hFIo4oezYaDq2', ''),
(41, 'budi pertama', 'budi@gmail.com', '$2y$10$yfjB1ZCNKJXRRMv9wjBVz.XGD/iYE.bko4LiEK/PG3PJlWmHpp0tK', 'http://192.168.43.52/android_register_login/profile_image/41.jpeg');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `cerita_table`
--
ALTER TABLE `cerita_table`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `user_table`
--
ALTER TABLE `user_table`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `cerita_table`
--
ALTER TABLE `cerita_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT untuk tabel `user_table`
--
ALTER TABLE `user_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
