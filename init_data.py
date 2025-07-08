#!/usr/bin/env python3
"""
Data initialization script for Dictators Club API
This script populates the database with sample dictators and achievements using the REST API endpoints.
"""

import requests
import json
import sys

# API base URL
BASE_URL = "http://localhost:8081/api"

# Sample data
DICTATORS = [
    {
        "username": "napoleon",
        "name": "Napoleon Bonaparte",
        "country": "France",
        "description": "Emperor of the French, military genius, and conqueror of Europe",
        "yearsInPower": "1799-1815"
    },
    {
        "username": "caesar",
        "name": "Julius Caesar",
        "country": "Roman Empire",
        "description": "Roman general and statesman who played a critical role in the events that led to the demise of the Roman Republic",
        "yearsInPower": "49-44 BC"
    },
    {
        "username": "genghis",
        "name": "Genghis Khan",
        "country": "Mongol Empire",
        "description": "Founder and first Great Khan of the Mongol Empire, which became the largest contiguous empire in history",
        "yearsInPower": "1206-1227"
    }
]

ACHIEVEMENTS = {
    "napoleon": [
        {
            "title": "Conquered most of Europe",
            "description": "Successfully conquered and controlled most of continental Europe through military campaigns",
            "year": 1807
        },
        {
            "title": "Napoleonic Code",
            "description": "Created the Napoleonic Code, a civil code that influenced legal systems worldwide",
            "year": 1804
        }
    ],
    "caesar": [
        {
            "title": "Crossed the Rubicon",
            "description": "Made the famous decision to cross the Rubicon river, starting a civil war that led to his rise to power",
            "year": 49
        },
        {
            "title": "Conquered Gaul",
            "description": "Successfully conquered all of Gaul (modern-day France) in the Gallic Wars",
            "year": 50
        }
    ],
    "genghis": [
        {
            "title": "Created the largest contiguous empire",
            "description": "Built the Mongol Empire, the largest contiguous land empire in history",
            "year": 1220
        },
        {
            "title": "United the Mongol tribes",
            "description": "Successfully united the warring Mongol tribes under his leadership",
            "year": 1206
        }
    ]
}

def check_api_health():
    """Check if the API is running and accessible"""
    try:
        response = requests.get(f"{BASE_URL}/dictators", timeout=5)
        return response.status_code == 200
    except requests.exceptions.RequestException:
        return False

def create_dictator(dictator_data):
    """Create a dictator using the data initialization API"""
    try:
        response = requests.post(f"{BASE_URL}/init/dictator", json=dictator_data)
        if response.status_code == 200:
            dictator = response.json()
            print(f"✓ Created dictator '{dictator_data['username']}' - ID: {dictator['id']}")
            return dictator
        else:
            error_msg = response.text if response.text else f"Status: {response.status_code}"
            print(f"✗ Failed to create dictator '{dictator_data['username']}': {error_msg}")
            return None
    except requests.exceptions.RequestException as e:
        print(f"✗ Error creating dictator '{dictator_data['username']}': {e}")
        return None

def create_achievement(dictator_id, achievement_data):
    """Create an achievement using the data initialization API"""
    try:
        response = requests.post(f"{BASE_URL}/init/dictator/{dictator_id}/achievement", json=achievement_data)
        if response.status_code == 200:
            achievement = response.json()
            print(f"✓ Created achievement '{achievement_data['title']}' for dictator ID {dictator_id}")
            return achievement
        else:
            error_msg = response.text if response.text else f"Status: {response.status_code}"
            print(f"✗ Failed to create achievement '{achievement_data['title']}': {error_msg}")
            return None
    except requests.exceptions.RequestException as e:
        print(f"✗ Error creating achievement '{achievement_data['title']}': {e}")
        return None

def initialize_sample_data():
    """Initialize all sample data using the bulk endpoint"""
    try:
        response = requests.post(f"{BASE_URL}/init/sample-data")
        if response.status_code == 200:
            result = response.json()
            print(f"✓ Sample data initialized successfully!")
            print(f"   - Dictators created: {result['dictatorsCreated']}")
            print(f"   - Achievements created: {result['achievementsCreated']}")
            print(f"   - Total dictators: {result['totalDictators']}")
            print(f"   - Total achievements: {result['totalAchievements']}")
            return True
        else:
            error_msg = response.text if response.text else f"Status: {response.status_code}"
            print(f"✗ Failed to initialize sample data: {error_msg}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"✗ Error initializing sample data: {e}")
        return False

def test_public_endpoints():
    """Test the public read-only endpoints"""
    print("\n=== Testing Public Endpoints ===")
    
    # Test get all dictators
    try:
        response = requests.get(f"{BASE_URL}/dictators")
        if response.status_code == 200:
            dictators = response.json()
            print(f"✓ GET /api/dictators - Found {len(dictators)} dictators")
        else:
            print(f"✗ GET /api/dictators - Status: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"✗ GET /api/dictators - Error: {e}")
    
    # Test get all achievements
    try:
        response = requests.get(f"{BASE_URL}/achievements")
        if response.status_code == 200:
            achievements = response.json()
            print(f"✓ GET /api/achievements - Found {len(achievements)} achievements")
        else:
            print(f"✗ GET /api/achievements - Status: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"✗ GET /api/achievements - Error: {e}")
    
    # Test get specific dictator
    try:
        response = requests.get(f"{BASE_URL}/dictators/1")
        if response.status_code == 200:
            dictator = response.json()
            print(f"✓ GET /api/dictators/1 - Found: {dictator.get('name', 'Unknown')}")
        else:
            print(f"✗ GET /api/dictators/1 - Status: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"✗ GET /api/dictators/1 - Error: {e}")

def main():
    print("🏛️  Dictators Club - Data Initialization Script")
    print("=" * 50)
    
    # Check API health
    print("Checking API health...")
    if not check_api_health():
        print("✗ API is not accessible. Make sure the Spring Boot application is running on port 8081.")
        sys.exit(1)
    
    print("✓ API is accessible")
    
    # Initialize sample data
    print("\n=== Initializing Sample Data ===")
    if initialize_sample_data():
        print("\n🎉 Sample data initialization completed successfully!")
    else:
        print("\n⚠️  Sample data initialization failed. Trying individual creation...")
        
        # Fallback: create dictators and achievements individually
        created_dictators = []
        for dictator_data in DICTATORS:
            dictator = create_dictator(dictator_data)
            if dictator:
                created_dictators.append(dictator)
                
                # Create achievements for this dictator
                if dictator['username'] in ACHIEVEMENTS:
                    for achievement_data in ACHIEVEMENTS[dictator['username']]:
                        create_achievement(dictator['id'], achievement_data)
    
    # Test public endpoints to verify data
    test_public_endpoints()
    
    print("\n=== Next Steps ===")
    print("📖 You can now:")
    print("   - Test the API endpoints with the sample data")
    print("   - View data in H2 console: http://localhost:8081/h2-console")
    print("   - Set up Keycloak/OIDC for authentication to test protected endpoints")
    
    print("\n🎯 Example API calls:")
    print("   # Get all dictators")
    print("   curl http://localhost:8081/api/dictators")
    print("   # Get specific dictator")
    print("   curl http://localhost:8081/api/dictators/1")
    print("   # Get all achievements")
    print("   curl http://localhost:8081/api/achievements")
    
    print("\n✅ Script completed successfully!")

if __name__ == "__main__":
    main()
