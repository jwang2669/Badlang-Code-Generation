.data
.align 2
newline: .asciiz "\n"
.text
.globl main
isEven:
sw $ra, ($sp)
subu $sp, $sp, 4
sw $fp, ($sp)
subu $sp, $sp, 4
addu $fp, $sp, 12
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
sltiu $t0, $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
beq $t0, $zero, else_branch_1
li $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j isEven_exit
j if_else_exit_1
else_branch_1:
if_else_exit_1:
subu $sp, $sp, 4
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
jal isOdd
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j isEven_exit
isEven_exit:
lw $ra, -4($fp)
move $t0, $fp
lw $fp, -8($fp)
move $sp, $t0
jr $ra
isOdd:
sw $ra, ($sp)
subu $sp, $sp, 4
sw $fp, ($sp)
subu $sp, $sp, 4
addu $fp, $sp, 12
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
sltiu $t0, $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
beq $t0, $zero, else_branch_2
li $t0, 0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j isOdd_exit
j if_else_exit_2
else_branch_2:
if_else_exit_2:
subu $sp, $sp, 4
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
jal isEven
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j isOdd_exit
isOdd_exit:
lw $ra, -4($fp)
move $t0, $fp
lw $fp, -8($fp)
move $sp, $t0
jr $ra
main:
move $fp, $sp
subu $sp, $sp, 4
li $t0, 4
sw $t0, ($sp)
subu $sp, $sp, 4
jal isEven
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
