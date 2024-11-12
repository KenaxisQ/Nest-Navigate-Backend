pipeline {
    agent any
    
    tools {
        maven 'local_home'
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
            post {
                success {
                    echo 'Archiving the artifacts'
                    archiveArtifacts artifacts: 'target/Test-0.0.1-SNAPSHOT.war', followSymlinks: false
                }
            }
        }

        stage('Deployments') {
            parallel {
                stage('Deploy to Staging') {
                    steps {
                        script {
                           deploy adapters: [tomcat9(credentialsId: 'd5c4c4d0-c831-4f86-8383-a5cbae9ba5cc', path: '', url:'http://192.168.0.103:8010/manager/text')], contextPath:/Test-0.0.1-SNAPSHOT.war' 
                        }
                    }
                }
            }
        }
    }
}
