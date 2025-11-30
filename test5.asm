.data
.align 2
newline: .asciiz "\n"
.align 2
result: .space 4
.text
.globl main
multiply:
sw $ra, ($sp)
subu $sp, $sp, 4
sw $fp, ($sp)
subu $sp, $sp, 4
addu $fp, $sp, 16
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, -4($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
mult $t0, $t1
mflo $t0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j multiply_exit
multiply_exit:
lw $ra, -8($fp)
move $t0, $fp
lw $fp, -12($fp)
move $sp, $t0
jr $ra
main:
move $fp, $sp
subu $sp, $sp, 4
li $t0, 2
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 3
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
add $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 4
sw $t0, ($sp)
subu $sp, $sp, 4
jal multiply
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, result
lw $t0, result
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
move $a0, $t0
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
li $v0, 10
syscall
