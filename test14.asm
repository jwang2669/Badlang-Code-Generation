.data
.align 2
newline: .asciiz "\n"
.align 2
x: .space 4
.align 2
y: .space 4
.text
.globl main
testScope:
sw $ra, ($sp)
subu $sp, $sp, 4
sw $fp, ($sp)
subu $sp, $sp, 4
addu $fp, $sp, 12
subu $sp, $sp, 4
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 2
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
sw $t0, 4($sp)
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 5
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
beq $t0, $zero, else_branch_1
subu $sp, $sp, 4
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 3
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
sw $t0, 4($sp)
lw $t0, -16($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j testScope_exit
j if_else_exit_1
else_branch_1:
if_else_exit_1:
lw $t0, -12($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j testScope_exit
testScope_exit:
lw $ra, -4($fp)
move $t0, $fp
lw $fp, -8($fp)
move $sp, $t0
jr $ra
main:
move $fp, $sp
subu $sp, $sp, 4
li $t0, 3
sw $t0, ($sp)
subu $sp, $sp, 4
jal testScope
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, x
subu $sp, $sp, 4
li $t0, 10
sw $t0, ($sp)
subu $sp, $sp, 4
jal testScope
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, y
lw $t0, x
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
lw $t0, y
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
