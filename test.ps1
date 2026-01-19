$BaseUrl = "http://localhost:8080"
$Headers = @{
    "Content-Type" = "application/json"
    "X-Actor-Id"   = "hugo"
}

Write-Host "=== CREATE CUSTOMER ===" -ForegroundColor Cyan

$createBody = @{
    name  = "Hugo Test"
    email = "hugo_$(Get-Random)@test.com"
} | ConvertTo-Json

$createResp = Invoke-RestMethod `
    -Method POST `
    -Uri "$BaseUrl/api/customers" `
    -Headers $Headers `
    -Body $createBody

$customerId = $createResp.data.id
$requestId  = $createResp.meta.requestId

Write-Host "Customer ID: $customerId"
Write-Host "Request ID:  $requestId"
Write-Host ""

Start-Sleep -Milliseconds 300

Write-Host "=== GET CUSTOMER ===" -ForegroundColor Cyan

Invoke-RestMethod `
    -Method GET `
    -Uri "$BaseUrl/api/customers/$customerId" `
    -Headers $Headers |
    ConvertTo-Json -Depth 5

Start-Sleep -Milliseconds 300

Write-Host "=== UPDATE CUSTOMER (PATCH) ===" -ForegroundColor Cyan

$updateBody = @{
    name = "Hugo Updated"
} | ConvertTo-Json

Invoke-RestMethod `
    -Method PUT `
    -Uri "$BaseUrl/api/customers/$customerId" `
    -Headers $Headers `
    -Body $updateBody |
    ConvertTo-Json -Depth 5

Start-Sleep -Milliseconds 300

Write-Host "=== DELETE CUSTOMER (SOFT) ===" -ForegroundColor Cyan

Invoke-RestMethod `
    -Method DELETE `
    -Uri "$BaseUrl/api/customers/$customerId" `
    -Headers $Headers |
    ConvertTo-Json -Depth 5

Start-Sleep -Milliseconds 300

Write-Host "=== LIST AUDIT EVENTS ===" -ForegroundColor Cyan

Invoke-RestMethod `
    -Method GET `
    -Uri "$BaseUrl/api/audit?page=0&size=20" `
    -Headers $Headers |
    ConvertTo-Json -Depth 6

Write-Host ""
Write-Host "=== DONE ===" -ForegroundColor Green
