@echo off
echo generate dao models and mappers
call mvn mybatis-generator:generate -pl .
@pause