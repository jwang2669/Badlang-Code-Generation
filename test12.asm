.data
.align 2
newline: .asciiz "\n"
.align 2
a: .space 4
.align 2
bb: .space 4
.text
.globl main
absolute:
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
slt $t0, $t0, $t1
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
sub $t1, $zero, $t0
sw $t1, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j absolute_exit
j if_else_exit_1
else_branch_1:
if_else_exit_1:
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j absolute_exit
absolute_exit:
lw $ra, -4($fp)
move $t0, $fp
lw $fp, -8($fp)
move $sp, $t0
jr $ra
main:
move $fp, $sp
subu $sp, $sp, 4
li $t0, 42
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sub $t1, $zero, $t0
sw $t1, ($sp)
subu $sp, $sp, 4
jal absolute
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, a
subu $sp, $sp, 4
li $t0, 7
sw $t0, ($sp)
subu $sp, $sp, 4
jal absolute
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, bb
lw $t0, a
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
lw $t0, bb
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
