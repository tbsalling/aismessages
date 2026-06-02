# Running AISdecoder in a Kubernetes cluster on AWS

**Published:** 2018-10-12
**Updated:** 2026-04-26

This tutorial shows a current Kubernetes deployment flow for an AIS decoder container on AWS. It assumes you already have access to a Kubernetes cluster, typically Amazon EKS.

Kubernetes remains a good fit for AISmessages-based services when you need repeatable deployment, scaling, and operational consistency. On AWS, the usual current choice is a managed EKS cluster created with `eksctl`, Terraform, or your preferred platform tooling.

## Prerequisites

- an AIS decoder container image available in a registry
- `kubectl` configured for your AWS cluster
- a namespace where you can deploy the service

## Check cluster access

```text
$ kubectl get nodes
```

## Deploy the service

Apply a Deployment and Service:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aisdecoder
spec:
  replicas: 2
  selector:
    matchLabels:
      app: aisdecoder
  template:
    metadata:
      labels:
        app: aisdecoder
    spec:
      containers:
        - name: aisdecoder
          image: ghcr.io/<owner>/aisdecoder:latest
          ports:
            - containerPort: 8080
          readinessProbe:
            tcpSocket:
              port: 8080
          livenessProbe:
            tcpSocket:
              port: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: aisdecoder
spec:
  selector:
    app: aisdecoder
  ports:
    - port: 8080
      targetPort: 8080
```

Save the file as `aisdecoder.yaml`, then apply it:

```text
$ kubectl apply -f aisdecoder.yaml
```

Verify the rollout:

```text
$ kubectl get deployments
$ kubectl get pods
$ kubectl get services
```

## Call the application

For local verification, port-forward the service:

```text
$ kubectl port-forward service/aisdecoder 8080:8080
```

Then call the API from another terminal:

```text
$ curl -X POST \
    http://localhost:8080/decode \
    -H 'Content-Type: application/json' \
    -d '[ "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53" ]'
```

When you want a public endpoint on AWS, the usual next step is to expose the service through an ingress controller or a `LoadBalancer` service backed by an AWS load balancer.

## Clean up

Remove the Kubernetes resources:

```text
$ kubectl delete -f aisdecoder.yaml
```

## Important resources

- [Amazon EKS documentation](https://docs.aws.amazon.com/eks/)
- [Kubernetes tutorials](https://kubernetes.io/docs/tutorials/)
- *Kubernetes in Action* by Marko Luksa
