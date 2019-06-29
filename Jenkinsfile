pipeline {
    agent any
    stages {

        stage('Cleaning Stage') {
            steps {
                withMaven(maven : 'MyMaven'){
                    script{
                        if (!isUnix()) {
                            echo "it's Windows"
                            bat 'mvn clean'
                        } else {
                            echo "it's unix/max"
                            sh 'mvn clean'
                        }
                    }
                }
            }
        }

        stage('Testing Stage') {
            steps {
                withMaven(maven : 'MyMaven'){
                    script{
                        if (!isUnix()) {
                            echo "it's Windows"
                            bat 'mvn test'
                        } else {
                            echo "it's unix/mac"
                            sh 'mvn test'
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}
// Make sure Jenkins has Maven Plugin : https://wiki.jenkins.io/display/JENKINS/Pipeline+Maven+Plugin
