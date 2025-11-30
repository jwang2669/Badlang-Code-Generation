.data
.align 2
newline: .asciiz "\n"
.align 2
count: .space 4
.text
.globl main
main:
move $fp, $sp
li $t0, 0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, count
while_loop_1:
lw $t0, count
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 5
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
slt $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
beq $t0, $zero, while_loop_exit_1
lw $t0, count
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
lw $t0, count
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
add $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, count
j while_loop_1
while_loop_exit_1:
li $v0, 10
syscall
