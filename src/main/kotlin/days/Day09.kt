package days
import utils.*
import java.util.*

data class Block(val id:Int?)

fun diskToString(disk: List<Block>) = disk.joinToString("") { if (it.id != null) "!" else "." };
fun checksum(disk: List<Block>) = disk.mapIndexed{ index, block -> (index * (block.id ?: 0)).toLong()}.sum()
fun emptySpace(fileLength:Int) = (0..fileLength).joinToString("") { "." }

fun solvePart1(d: List<Block>): Long {
  fun isCompressComplete(disk: List<Block>):Boolean {
    val firstFreeSpace = disk.indexOfFirst { it.id==null }
    return disk.drop(firstFreeSpace+1).all { it.id==null }
  }

  val disk = d.toMutableList()
  while (!isCompressComplete(disk)) {
    val fileIndex = disk.indexOfLast {  it.id != null }
    val freeSpaceIndex = disk.indexOfFirst { it.id == null }
    Collections.swap(disk, fileIndex,freeSpaceIndex)
  }
  return checksum(disk)
}

fun solvePart2(d: List<Block>): Long {
  val disk = d.toMutableList()
  var currentFileId = disk.maxOf { it.id ?: 0 }
  while (currentFileId >= 0) {
    val fileIndexStart = disk.indexOfFirst {  it.id == currentFileId }
    val fileIndexEnd = disk.indexOfLast { it.id == currentFileId }
    val fileLength =  fileIndexEnd - fileIndexStart

    val freeSpotStartIndex = diskToString(disk).indexOf(emptySpace(fileLength))
    if (freeSpotStartIndex != -1 && freeSpotStartIndex < fileIndexStart) {
      var fileChunk = 0
      while (fileChunk <= fileLength) {
        Collections.swap(disk, fileIndexStart+fileChunk,freeSpotStartIndex+fileChunk)
        fileChunk += 1;
      }
    }
    currentFileId -= 1;
  }
  return checksum(disk)
}

suspend fun main() = AdventOfCode(day = 9, year = 2024) {
  val disk = mutableListOf<Block>()
  input.chunked(1).forEachIndexed{ index, length ->
    repeat(length.toInt()) {
      val id = if (index % 2 == 0) index/2 else null
      disk.add(Block(id))
    }
  }

  part1 = solvePart1(disk)
  part2 = solvePart2(disk)
}.start()
