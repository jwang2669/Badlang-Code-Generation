.data
.align 2
newline: .asciiz "\n"
.align 2
a: .space 4
.align 2
bb: .space 4
.align 2
c: .space 4
.text
.globl main
main:
move $fp, $sp
li $t0, 10
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, a
li $t0, 3
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, bb
lw $t0, a
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, bb
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
lw $t0, a
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, bb
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
div $t0, $t1
mflo $t0
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
add $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, a
sw $t0, ($sp)
subu $sp, $sp, 4
li $t0, 3
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, bb
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
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
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
sw $t0, c
lw $t0, c
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
lw $t0, a
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, bb
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, c
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
lw $t1, 4($sp)
addu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
add $t0, $t0, $t1
sw $t0, ($sp)
subu $sp, $sp, 4
lw $t0, 4($sp)
addu $sp, $sp, 4
sw $t0, a
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
li $v0, 10
syscall
