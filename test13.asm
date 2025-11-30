.data
.align 2
newline: .asciiz "\n"
.align 2
total: .space 4
.text
.globl main
power:
sw $ra, ($sp)
subu $sp, $sp, 4
sw $fp, ($sp)
subu $sp, $sp, 4
addu $fp, $sp, 16
subu $sp, $sp, 4
li $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($sp)
while_loop_1:
lw $t0, -4($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 0
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
beq $t0, $zero, while_loop_exit_1
lw $t0, -16($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 0($fp)
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
sw $t0, -16($fp)
lw $t0, -4($fp)
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
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, -4($fp)
j while_loop_1
while_loop_exit_1:
lw $t0, -16($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j power_exit
power_exit:
lw $ra, -8($fp)
move $t0, $fp
lw $fp, -12($fp)
move $sp, $t0
jr $ra
sumPowers:
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
slt $t0, $t1, $t0
xori $t0, $t0, 1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
beq $t0, $zero, else_branch_1
li $t0, 0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j sumPowers_exit
j if_else_exit_1
else_branch_1:
subu $sp, $sp, 4
lw $t0, 0($fp)
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 2
sw $t0, ($sp)
subu $sp, $sp, 4
jal power
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
jal sumPowers
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
add $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, 4($fp)
j sumPowers_exit
if_else_exit_1:
sumPowers_exit:
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
jal sumPowers
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, total
lw $t0, total
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
