
echo 'CDCI-TPM-BUILD'

node('master'){

	stage 'Compile'
	node {
		git url: 'https://github.com/bdushimi/cdci-jenkins.git'
		def mvnHome = tool name: 'Maven', type: 'maven'
		sh "${mvnHome}/bin/mvn -B compile"
	}

	stage 'Test'
	node{
		git url: 'https://github.com/bdushimi/cdci-jenkins.git'
		def mvnHome = tool name: 'Maven', type: 'maven'
		sh "${mvnHome}/bin/mvn -B verify"
		step([$class: 'ArtifactArchiver', artifacts: '**/target/*.war', fingerprint: true])
		step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
	}
}



pipeline {
    
 agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
