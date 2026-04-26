# Running AISdecoder in a Kubernetes cluster on AWS

**Published:** 2018-10-12

> **Historical note:** This tutorial is a reference deployment walkthrough for the `aisdecoder` container. It uses the historical `kops`-on-AWS flow.

This tutorial walks through creating a small Kubernetes cluster on AWS, deploying the AIS decoder container, calling it through a proxy, and cleaning everything up afterwards.

## Background

The original series built up in stages:

1. [What is AIS?](../articles/what-is-ais.md)
2. [Creating a Spring Boot based AIS message decoder](spring-boot-decoder.md)
3. [Creating, sharing, and running a Docker image to decode AIS messages](docker-decoder.md)

Kubernetes is useful as a standardized home for containerized applications, including deployment, scaling, and replacement of failed instances.

## Install AWS CLI

The original walkthrough used Homebrew:

```text
$ brew install awscli
```

Create or choose an AWS IAM user with permissions for:

```text
AmazonEC2FullAccess
AmazonRoute53FullAccess
AmazonS3FullAccess
AmazonVPCFullAccess
```

Configure the CLI:

```text
$ aws configure
AWS Access Key ID [None]: XXXXXXXXXXXXXXXXX
AWS Secret Access Key [None]: XXXXXXXXXXXXXXXXXXXXXXXXXX
Default region name [None]: eu-central-1
Default output format [None]:
```

## Install kops

```text
$ brew install kops
```

## Prepare AWS for kops

Create an S3 bucket to hold cluster state:

```text
$ aws s3api create-bucket \
    --bucket tbsalling-kops-state-store \
    --region eu-central-1 \
    --create-bucket-configuration LocationConstraint=eu-central-1
```

Enable versioning:

```text
$ aws s3api put-bucket-versioning \
    --bucket tbsalling-kops-state-store \
    --versioning-configuration Status=Enabled
```

Generate an SSH keypair and export environment variables:

```text
$ ssh-keygen -t rsa
$ export KOPS_CLUSTER_NAME=tbsalling-kops.k8s.local
$ export KOPS_STATE_STORE=s3://tbsalling-kops-state-store
```

## Create the cluster

The original cluster used 3 nodes in `eu-central-1a`:

```text
$ kops create cluster \
    --node-count=3 \
    --master-size=t2.micro \
    --node-size=t2.micro \
    --zones=eu-central-1a \
    --ssh-public-key=~/.ssh/id_rsa_kops.pub \
    --name=${KOPS_CLUSTER_NAME} \
    --yes
```

Validate until the cluster becomes ready:

```text
$ kops validate cluster
```

## Deploy AISdecoder

Check that `kubectl` sees the nodes:

```text
$ kubectl get nodes
```

Deploy the Docker image from Docker Hub:

```text
$ kubectl run aisdecoder --image=tbsalling/aisdecoder --port=8080
```

Verify the deployment:

```text
$ kubectl get deployments
```

## Call the application through the Kubernetes proxy

Start the proxy:

```text
$ kubectl proxy
```

Then, in another terminal:

```text
$ curl -X POST \
    http://localhost:8001/api/v1/namespaces/default/pods/aisdecoder-c4c46474-rzd2x/proxy/decode \
    -H 'Content-Type: application/json' \
    -d '[ "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53" ]'
```

The pod name segment can be retrieved with `kubectl get pods`.

## Clean up

Remove the deployment:

```text
$ kubectl delete deployment/aisdecoder
```

Delete the cluster and its AWS resources:

```text
$ kops delete cluster --yes --name=${KOPS_CLUSTER_NAME}
```

## Important resources

- [Kubernetes on AWS with kops](https://kubernetes.io/docs/setup/custom-cloud/kops/)
- [Kubernetes tutorials](https://kubernetes.io/docs/tutorials/)
- *Kubernetes in Action* by Marko Luksa
