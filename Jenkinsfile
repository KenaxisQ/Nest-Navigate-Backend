pipeline {
    agent any
    
    tools {
        maven 'local_maven'
  
    }

stages{
        stage('Build'){
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    echo 'Archiving the artifacts'
                    archiveArtifacts artifacts: '**/target/*.war'
                }
            }
        }

        stage ('Deployments'){
            parallel{
                stage ("Deploy to Staging"){
                    steps {
                        deploy adapters: [tomcat9(credentialsId: 'a0648c3d-5712-4bca-be90-971918a9fa68', path: '', url: 'http://192.168.0.103:8010/')], contextPath: null, war: '**/*.war'
                    }
                }
            }
        }
    }
}
