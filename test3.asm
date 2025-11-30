.data
.align 2
newline: .asciiz "\n"
.text
.globl main
max:
sw $ra, ($sp)
subu $sp, $sp, 4
sw $fp, ($sp)
subu $sp, $sp, 4
addu $fp, $sp, 20
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
slt $t0, $t1, $t0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, -8($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
slt $t0, $t1, $t0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
and $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
beq $t0, $zero, else_branch_1
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j max_exit
j if_else_exit_1
else_branch_1:
if_else_exit_1:
lw $t0, -4($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, -8($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
slt $t0, $t1, $t0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
beq $t0, $zero, else_branch_2
lw $t0, -4($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j max_exit
j if_else_exit_2
else_branch_2:
if_else_exit_2:
lw $t0, -8($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j max_exit
max_exit:
lw $ra, -12($fp)
move $t0, $fp
lw $fp, -16($fp)
move $sp, $t0
jr $ra
main:
move $fp, $sp
subu $sp, $sp, 4
li $t0, 3
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 7
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 5
sw $t0, ($sp)
subu $sp, $sp, 4
jal max
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
