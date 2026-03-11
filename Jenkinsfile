
pipeline {
agent any
options {
skipStagesAfterUnstable()
}
stages {
stage('Build') {
steps {
sh 'mvn -B -DskipTests clean package'
}
}
stage('Test') {
steps {
sh 'mvn test'
}
post {
always {
junit 'target/cucumber-junit/*.xml'
}
}
}
stage('Deliver') {
steps { bat 'jenkins\\scripts\\delivery.bat' }
}
}
}
