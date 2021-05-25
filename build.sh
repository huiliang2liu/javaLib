echo '开始编译'
rm -rf classes
rm -rf java.list
rm -rf java2.list
mkdir classes
find src -name '*.java' > java.list
find src2 -name '*.java' > java2.list
cat java.list
javac -target 1.6 -source 1.6 -d classes -sourcepath src:src2 @java.list @java2.list
echo '编译完成'
rm -rf java.list
rm -rf java2.list
cd classes
java com.xiaohei.java.lib.Main