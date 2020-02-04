using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace MockAPIGateway.Controllers
{
    [Route("api/")]
    [ApiController]
    public class VehiclesController : ControllerBase
    {
        [HttpGet("vehicles")]
        public ActionResult GetVehicles()
        {
            return Ok(new
            {
                Vehicles = new []
                {
                    new
                    {
                        Id=1,
                        Name="vehicleName1"
                    },
                    new
                    {
                        Id=2,
                        Name="vehicleName2"
                    }
                },
            });
        }

        [HttpGet("vehicle/{id}")]
        public ActionResult GetVehicle()
        {
            return Ok(new
            {
                Id = 1,
                Name = "vehicleName1"
            });
        }

        [HttpGet("vehicle/{id}/gps")]
        public ActionResult GetVehicleGps([FromQuery(Name = "datetime")] string page)
        {
            return Ok();
        }

        [HttpPut("vehicle/{id}")]
        public ActionResult UpdateVehicle()
        {
            return Ok();
        }

        [HttpGet("vehicle/{id}/environmentalData")]
        public ActionResult GetVehicleEnvironmentalData()
        {
            return Ok(new
            {
                Temperature = 23.0,
                Humidity = 52.0,
                LightIntensity = 80.0,
                Gps = "<Google Maps Reverse Geocoding API Response>"
            });
        }
    }
}
