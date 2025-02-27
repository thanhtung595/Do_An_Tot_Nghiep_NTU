from rest_framework import serializers

class AccountSerializer(serializers.Serializer):
    id = serializers.IntegerField()
    username = serializers.CharField()
    email = serializers.CharField()
    full_name = serializers.CharField()
    created_at = serializers.DateTimeField()
